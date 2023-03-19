package cinescout.suggestions.presentation.viewmodel

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.usecase.GetSuggestionsWithExtras
import cinescout.suggestions.presentation.mapper.ForYouItemUiModelMapper
import cinescout.suggestions.presentation.model.ForYouAction
import cinescout.suggestions.presentation.model.ForYouScreenplayUiModel
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.model.ForYouType
import cinescout.suggestions.presentation.util.FixedSizeStack
import cinescout.suggestions.presentation.util.Stack
import cinescout.suggestions.presentation.util.joinBy
import cinescout.suggestions.presentation.util.pop
import cinescout.utils.compose.MoleculeViewModel
import cinescout.voting.domain.usecase.SetDisliked
import cinescout.voting.domain.usecase.SetLiked
import cinescout.watchlist.domain.usecase.AddToWatchlist
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Named

@KoinViewModel
internal class ForYouViewModel(
    private val addToWatchlist: AddToWatchlist,
    private val forYouItemUiModelMapper: ForYouItemUiModelMapper,
    private val getSuggestionsWithExtras: GetSuggestionsWithExtras,
    private val networkErrorMapper: NetworkErrorToMessageMapper,
    private val setDisliked: SetDisliked,
    private val setLiked: SetLiked,
    @Named(SuggestionsStackSizeName) suggestionsStackSize: Int = 10
) : MoleculeViewModel<ForYouAction, ForYouState>() {

    override val state = launchMolecule {
        var type by remember { mutableStateOf(ForYouType.Movies) }
        var suggestedMoviesStack by remember { mutableStateOf(Stack.empty<ForYouScreenplayUiModel>()) }
        var suggestedTvShowsStack by remember { mutableStateOf(Stack.empty<ForYouScreenplayUiModel>()) }
        
        val suggestedMovies = getSuggestionsWithExtras(
            type = ScreenplayType.Movies,
            shouldRefreshExtras = false,
            take = suggestionsStackSize
        ).collectAsState(null).value
            ?: return@launchMolecule ForYouState.Loading

        val suggestedTvShows = getSuggestionsWithExtras(
            type = ScreenplayType.TvShows,
            shouldRefreshExtras = false,
            take = suggestionsStackSize
        ).collectAsState(null).value
            ?: return@launchMolecule ForYouState.Loading
        
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
        
        LaunchedEffect(Unit) {
            actions.collect { action ->
                when (action) {
                    is ForYouAction.AddToWatchlist -> {
                        when (type) {
                            ForYouType.Movies -> {
                                val (newStack, _) = suggestedMoviesStack.pop()
                                suggestedMoviesStack = newStack
                            }
                            ForYouType.TvShows -> {
                                val (newStack, _) = suggestedTvShowsStack.pop()
                                suggestedTvShowsStack = newStack
                            }
                        }
                        launch { addToWatchlist(action.itemId) }
                    }
                    is ForYouAction.Dislike -> {
                        when (type) {
                            ForYouType.Movies -> {
                                val (newStack, _) = suggestedMoviesStack.pop()
                                suggestedMoviesStack = newStack
                            }
                            ForYouType.TvShows -> {
                                val (newStack, _) = suggestedTvShowsStack.pop()
                                suggestedTvShowsStack = newStack
                            }
                        }
                        launch { setDisliked(action.itemId) }
                    }
                    is ForYouAction.Like -> {
                        when (type) {
                            ForYouType.Movies -> {
                                val (newStack, _) = suggestedMoviesStack.pop()
                                suggestedMoviesStack = newStack
                            }
                            ForYouType.TvShows -> {
                                val (newStack, _) = suggestedTvShowsStack.pop()
                                suggestedTvShowsStack = newStack
                            }
                        }
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

        ForYouState(
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

    companion object {

        const val SuggestionsStackSizeName = "suggestionsStackSize"
    }
}

internal operator fun StateFlow<FixedSizeStack<ForYouScreenplayUiModel>>.contains(movie: Movie) =
    movie.tmdbId in value.all().map { model -> model.tmdbScreenplayId }
