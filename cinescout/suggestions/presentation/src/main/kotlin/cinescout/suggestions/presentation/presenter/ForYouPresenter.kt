package cinescout.suggestions.presentation.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import arrow.core.Either
import arrow.core.Nel
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.suggestions.domain.model.SuggestedScreenplayWithExtras
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.presentation.mapper.ForYouItemUiModelMapper
import cinescout.suggestions.presentation.model.ForYouAction
import cinescout.suggestions.presentation.model.ForYouScreenplayUiModel
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.model.ForYouType
import cinescout.suggestions.presentation.util.Stack
import cinescout.suggestions.presentation.util.joinBy
import cinescout.suggestions.presentation.util.pop
import cinescout.voting.domain.usecase.SetDisliked
import cinescout.voting.domain.usecase.SetLiked
import cinescout.watchlist.domain.usecase.AddToWatchlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
internal class ForYouPresenter(
    private val addToWatchlist: AddToWatchlist,
    private val forYouItemUiModelMapper: ForYouItemUiModelMapper,
    private val networkErrorMapper: NetworkErrorToMessageMapper,
    private val setDisliked: SetDisliked,
    private val setLiked: SetLiked
) {

    @Composable
    fun models(
        actionsFlow: Flow<ForYouAction>,
        suggestedMoviesFlow: Flow<Either<SuggestionError, Nel<SuggestedScreenplayWithExtras>>>,
        suggestedTvShowsFlow: Flow<Either<SuggestionError, Nel<SuggestedScreenplayWithExtras>>>
    ): ForYouState {
        var type by remember { mutableStateOf(ForYouType.Movies) }
        var suggestedMoviesStack by remember { mutableStateOf(Stack.empty<ForYouScreenplayUiModel>()) }
        var suggestedTvShowsStack by remember { mutableStateOf(Stack.empty<ForYouScreenplayUiModel>()) }

        val suggestedMovies = suggestedMoviesFlow.collectAsState(null).value
            ?: return ForYouState.Loading

        val suggestedTvShows = suggestedTvShowsFlow.collectAsState(null).value
            ?: return ForYouState.Loading

        val suggestedMovieState = suggestedMovies.fold(
            ifLeft = ::toMoviesSuggestionsState,
            ifRight = { list ->
                suggestedMoviesStack = suggestedMoviesStack.joinBy(
                    collection = list.map { movie -> forYouItemUiModelMapper.toUiModel(movie) },
                    selector = { it.tmdbScreenplayId }
                )
                ForYouState.SuggestedItem.Screenplay(
                    screenplay = checkNotNull(suggestedMoviesStack.head())
                )
            }
        )

        val suggestedTvShowState = suggestedTvShows.fold(
            ifLeft = ::toTvShowsSuggestionsState,
            ifRight = { list ->
                suggestedTvShowsStack = suggestedTvShowsStack.joinBy(
                    collection = list.map { tvShow -> forYouItemUiModelMapper.toUiModel(tvShow) },
                    selector = { it.tmdbScreenplayId }
                )
                ForYouState.SuggestedItem.Screenplay(
                    screenplay = checkNotNull(suggestedTvShowsStack.head())
                )
            }
        )

        fun popStack() = when (type) {
            ForYouType.Movies -> {
                val (newStack, _) = suggestedMoviesStack.pop()
                suggestedMoviesStack = newStack
            }

            ForYouType.TvShows -> {
                val (newStack, _) = suggestedTvShowsStack.pop()
                suggestedTvShowsStack = newStack
            }
        }

        LaunchedEffect(Unit) {
            actionsFlow.collect { action ->
                when (action) {
                    is ForYouAction.AddToWatchlist -> {
                        popStack()
                        launch { addToWatchlist(action.itemId) }
                    }

                    is ForYouAction.Dislike -> {
                        popStack()
                        launch { setDisliked(action.itemId) }
                    }

                    is ForYouAction.Like -> {
                        popStack()
                        launch { setLiked(action.itemId) }
                    }

                    is ForYouAction.SelectForYouType -> {
                        type = action.forYouType
                    }
                }
            }
        }

        val suggestedItem = when (type) {
            ForYouType.Movies -> suggestedMovieState
            ForYouType.TvShows -> suggestedTvShowState
        }

        return ForYouState(
            moviesStack = suggestedMoviesStack,
            tvShowsStack = suggestedTvShowsStack,
            type = type,
            suggestedItem = suggestedItem
        )
    }

    private fun toMoviesSuggestionsState(error: SuggestionError): ForYouState.SuggestedItem = when (error) {
        is SuggestionError.Source -> {
            val message = networkErrorMapper.toMessage(error.networkError)
            ForYouState.SuggestedItem.Error(message)
        }

        is SuggestionError.NoSuggestions -> ForYouState.SuggestedItem.NoSuggestedMovies
    }

    private fun toTvShowsSuggestionsState(error: SuggestionError): ForYouState.SuggestedItem = when (error) {
        is SuggestionError.Source -> {
            val message = networkErrorMapper.toMessage(error.networkError)
            ForYouState.SuggestedItem.Error(message)
        }

        is SuggestionError.NoSuggestions -> ForYouState.SuggestedItem.NoSuggestedTvShows
    }
}
