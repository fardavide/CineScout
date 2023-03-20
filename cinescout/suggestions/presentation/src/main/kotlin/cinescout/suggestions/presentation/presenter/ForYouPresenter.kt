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
import cinescout.suggestions.domain.model.SuggestedScreenplayWithExtras
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.presentation.action.ForYouAction
import cinescout.suggestions.presentation.mapper.ForYouItemUiModelMapper
import cinescout.suggestions.presentation.model.ForYouType
import cinescout.suggestions.presentation.state.ForYouState
import cinescout.utils.compose.NetworkErrorToMessageMapper
import cinescout.voting.domain.usecase.SetDisliked
import cinescout.voting.domain.usecase.SetLiked
import cinescout.watchlist.domain.usecase.AddToWatchlist
import kotlinx.coroutines.flow.Flow
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

        val suggestedMovies = suggestedMoviesFlow.collectAsState(null).value
            ?: return ForYouState.Loading

        val suggestedTvShows = suggestedTvShowsFlow.collectAsState(null).value
            ?: return ForYouState.Loading

        val suggestedItem = when (type) {
            ForYouType.Movies -> suggestedMovies.fold(
                ifLeft = ::toMoviesSuggestionsState,
                ifRight = { list -> ForYouState.SuggestedItem.Screenplay(forYouItemUiModelMapper.toUiModel(list.head)) }
            )
            ForYouType.TvShows -> suggestedTvShows.fold(
                ifLeft = ::toTvShowsSuggestionsState,
                ifRight = { list -> ForYouState.SuggestedItem.Screenplay(forYouItemUiModelMapper.toUiModel(list.head)) }
            )
        }

        LaunchedEffect(Unit) {
            actionsFlow.collect { action ->
                when (action) {
                    is ForYouAction.AddToWatchlist -> addToWatchlist(action.itemId)
                    is ForYouAction.Dislike -> setDisliked(action.itemId)
                    is ForYouAction.Like -> setLiked(action.itemId)
                    is ForYouAction.SelectForYouType -> type = action.forYouType
                }
            }
        }

        return ForYouState(
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
