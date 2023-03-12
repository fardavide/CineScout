package cinescout.suggestions.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.movies.domain.usecase.AddMovieToDislikedList
import cinescout.movies.domain.usecase.AddMovieToLikedList
import cinescout.movies.domain.usecase.AddMovieToWatchlist
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.usecase.GetSuggestedMoviesWithExtras
import cinescout.suggestions.domain.usecase.GetSuggestedTvShowsWithExtras
import cinescout.suggestions.presentation.mapper.ForYouItemUiModelMapper
import cinescout.suggestions.presentation.model.ForYouAction
import cinescout.suggestions.presentation.model.ForYouEvent
import cinescout.suggestions.presentation.model.ForYouOperation
import cinescout.suggestions.presentation.model.ForYouScreenplayUiModel
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.reducer.ForYouReducer
import cinescout.suggestions.presentation.util.FixedSizeStack
import cinescout.tvshows.domain.usecase.AddTvShowToDislikedList
import cinescout.tvshows.domain.usecase.AddTvShowToLikedList
import cinescout.tvshows.domain.usecase.AddTvShowToWatchlist
import cinescout.utils.android.CineScoutViewModel
import cinescout.utils.android.Reducer
import cinescout.utils.kotlin.exhaustive
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Named

@KoinViewModel
internal class ForYouViewModel(
    private val addMovieToDislikedList: AddMovieToDislikedList,
    private val addMovieToLikedList: AddMovieToLikedList,
    private val addMovieToWatchlist: AddMovieToWatchlist,
    private val addTvShowToDislikedList: AddTvShowToDislikedList,
    private val addTvShowToLikedList: AddTvShowToLikedList,
    private val addTvShowToWatchlist: AddTvShowToWatchlist,
    private val forYouItemUiModelMapper: ForYouItemUiModelMapper,
    private val getSuggestedMoviesWithExtras: GetSuggestedMoviesWithExtras,
    private val getSuggestedTvShowsWithExtras: GetSuggestedTvShowsWithExtras,
    private val networkErrorMapper: NetworkErrorToMessageMapper,
    reducer: ForYouReducer,
    @Named(SuggestionsStackSizeName) suggestionsStackSize: Int = 10
) : CineScoutViewModel<ForYouAction, ForYouState>(initialState = ForYouState.Loading),
    Reducer<ForYouState, ForYouOperation> by reducer {

    init {
        viewModelScope.launch {
            combine(
                getSuggestedMoviesWithExtras(refreshMovieExtras = false, take = suggestionsStackSize),
                getSuggestedTvShowsWithExtras(refreshTvShowExtras = false, take = suggestionsStackSize)
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
        viewModelScope.launch {
            when (val itemId = action.itemId) {
                is TmdbScreenplayId.Movie -> addMovieToWatchlist(itemId)
                is TmdbScreenplayId.TvShow -> addTvShowToWatchlist(itemId)
            }
        }
        updateState { currentState ->
            currentState.reduce(action)
        }
    }

    private fun onDislike(action: ForYouAction.Dislike) {
        viewModelScope.launch {
            when (val itemId = action.itemId) {
                is TmdbScreenplayId.Movie -> addMovieToDislikedList(itemId)
                is TmdbScreenplayId.TvShow -> addTvShowToDislikedList(itemId)
            }
        }
        updateState { currentState ->
            currentState.reduce(action)
        }
    }

    private fun onLike(action: ForYouAction.Like) {
        viewModelScope.launch {
            when (val itemId = action.itemId) {
                is TmdbScreenplayId.Movie -> addMovieToLikedList(itemId)
                is TmdbScreenplayId.TvShow -> addTvShowToLikedList(itemId)
            }
        }
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
