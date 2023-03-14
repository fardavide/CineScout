package cinescout.suggestions.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.usecase.GetSuggestionsWithExtras
import cinescout.suggestions.presentation.mapper.ForYouItemUiModelMapper
import cinescout.suggestions.presentation.model.ForYouAction
import cinescout.suggestions.presentation.model.ForYouEvent
import cinescout.suggestions.presentation.model.ForYouOperation
import cinescout.suggestions.presentation.model.ForYouScreenplayUiModel
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.reducer.ForYouReducer
import cinescout.suggestions.presentation.util.FixedSizeStack
import cinescout.utils.android.CineScoutViewModel
import cinescout.utils.android.Reducer
import cinescout.utils.kotlin.exhaustive
import cinescout.voting.domain.usecase.SetDisliked
import cinescout.voting.domain.usecase.SetLiked
import cinescout.watchlist.domain.usecase.AddToWatchlist
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Named

@KoinViewModel
internal class ForYouViewModel(
    private val addToWatchlist: AddToWatchlist,
    private val forYouItemUiModelMapper: ForYouItemUiModelMapper,
    private val getSuggestionsWithExtras: GetSuggestionsWithExtras,
    private val networkErrorMapper: NetworkErrorToMessageMapper,
    reducer: ForYouReducer,
    private val setDisliked: SetDisliked,
    private val setLiked: SetLiked,
    @Named(SuggestionsStackSizeName) suggestionsStackSize: Int = 10
) : CineScoutViewModel<ForYouAction, ForYouState>(initialState = ForYouState.Loading),
    Reducer<ForYouState, ForYouOperation> by reducer {

    init {
        viewModelScope.launch {
            combine(
                getSuggestionsWithExtras(
                    type = ScreenplayType.Movies,
                    shouldRefreshExtras = false,
                    take = suggestionsStackSize
                ),
                getSuggestionsWithExtras(
                    type = ScreenplayType.TvShows,
                    shouldRefreshExtras = false,
                    take = suggestionsStackSize
                )
            ) { moviesEither, tvShowsEither ->
                moviesEither.fold(
                    ifLeft = { error ->
                        updateState { currentState ->
                            val operation = ForYouEvent.SuggestedMoviesError(
                                error = error,
                                toErrorState = ::toMoviesSuggestionsState
                            )
                            currentState.reduce(operation)
                        }
                    },
                    ifRight = { movies ->
                        updateState { currentState ->
                            val operation = ForYouEvent.SuggestedMoviesReceived(
                                movies = movies.map(forYouItemUiModelMapper::toUiModel)
                            )
                            currentState.reduce(operation)
                        }
                    }
                )
                tvShowsEither.fold(
                    ifLeft = { error ->
                        updateState { currentState ->
                            val operation = ForYouEvent.SuggestedTvShowsError(
                                error = error,
                                toErrorState = ::toTvShowsSuggestionsState
                            )
                            currentState.reduce(operation)
                        }
                    },
                    ifRight = { tvShows ->
                        updateState { currentState ->
                            val operation = ForYouEvent.SuggestedTvShowsReceived(
                                tvShows = tvShows.map(forYouItemUiModelMapper::toUiModel)
                            )
                            currentState.reduce(operation)
                        }
                    }
                )
            }.collect()
        }
    }

    override fun submit(action: ForYouAction) {
        when (action) {
            is ForYouAction.AddToWatchlist -> onAddToWatchlist(action)
            is ForYouAction.Dislike -> onDislike(action)
            is ForYouAction.Like -> onLike(action)
            is ForYouAction.SelectForYouType -> onSelectForYouType(action)
        }.exhaustive
    }

    private fun onAddToWatchlist(action: ForYouAction.AddToWatchlist) {
        viewModelScope.launch { addToWatchlist(action.itemId) }
        updateState { currentState ->
            currentState.reduce(action)
        }
    }

    private fun onDislike(action: ForYouAction.Dislike) {
        viewModelScope.launch { setDisliked(action.itemId) }
        updateState { currentState ->
            currentState.reduce(action)
        }
    }

    private fun onLike(action: ForYouAction.Like) {
        viewModelScope.launch { setLiked(action.itemId) }
        updateState { currentState ->
            currentState.reduce(action)
        }
    }

    private fun onSelectForYouType(action: ForYouAction.SelectForYouType) {
        updateState { currentState ->
            currentState.reduce(action)
        }
    }

    private fun toMoviesSuggestionsState(error: SuggestionError): ForYouState.SuggestedItem = when (error) {
        is SuggestionError.Source -> {
            val message = networkErrorMapper.toMessage(error.dataError.networkError)
            ForYouState.SuggestedItem.Error(message)
        }
        is SuggestionError.NoSuggestions -> ForYouState.SuggestedItem.NoSuggestedMovies
    }

    private fun toTvShowsSuggestionsState(error: SuggestionError): ForYouState.SuggestedItem = when (error) {
        is SuggestionError.Source -> {
            val message = networkErrorMapper.toMessage(error.dataError.networkError)
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
