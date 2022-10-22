package cinescout.suggestions.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import cinescout.common.model.SuggestionError
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.usecase.AddMovieToDislikedList
import cinescout.movies.domain.usecase.AddMovieToLikedList
import cinescout.movies.domain.usecase.AddMovieToWatchlist
import cinescout.settings.domain.usecase.ShouldShowForYouHint
import cinescout.suggestions.domain.usecase.GetSuggestedMoviesWithExtras
import cinescout.suggestions.domain.usecase.GetSuggestedTvShowsWithExtras
import cinescout.suggestions.presentation.mapper.ForYouItemUiModelMapper
import cinescout.suggestions.presentation.model.ForYouAction
import cinescout.suggestions.presentation.model.ForYouItemId
import cinescout.suggestions.presentation.model.ForYouMovieUiModel
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.model.ForYouTvShowUiModel
import cinescout.suggestions.presentation.util.FixedSizeStack
import cinescout.suggestions.presentation.util.isEmpty
import cinescout.suggestions.presentation.util.joinBy
import cinescout.suggestions.presentation.util.pop
import cinescout.tvshows.domain.usecase.AddTvShowToDislikedList
import cinescout.tvshows.domain.usecase.AddTvShowToLikedList
import cinescout.tvshows.domain.usecase.AddTvShowToWatchlist
import cinescout.utils.android.CineScoutViewModel
import cinescout.utils.kotlin.exhaustive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import store.Refresh

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
    private val shouldShowForYouHint: ShouldShowForYouHint,
    suggestionsStackSize: Int = 10
) : CineScoutViewModel<ForYouAction, ForYouState>(initialState = ForYouState.Loading) {

    private val moviesSuggestionsStack: MutableStateFlow<FixedSizeStack<ForYouMovieUiModel>> =
        MutableStateFlow(FixedSizeStack.empty(suggestionsStackSize))
    private val tvShowsSuggestionsStack: MutableStateFlow<FixedSizeStack<ForYouTvShowUiModel>> =
        MutableStateFlow(FixedSizeStack.empty(suggestionsStackSize))

    init {
        viewModelScope.launch {
            combine(
                getSuggestedMoviesWithExtras(movieExtraRefresh = Refresh.IfExpired(), take = suggestionsStackSize),
                getSuggestedTvShowsWithExtras(tvShowExtraRefresh = Refresh.IfExpired(), take = suggestionsStackSize),
                shouldShowForYouHint()
            ) { moviesEither, tvShowsEither, shouldShowForYouHintValue ->
                moviesEither.fold(
                    ifLeft = { error ->
                        updateState { currentState ->
                            if (moviesSuggestionsStack.isEmpty() || error is SuggestionError.Source) {
                                currentState.copy(
                                    shouldShowHint = false,
                                    suggestedMovie = toMoviesSuggestionsState(error)
                                )
                            } else {
                                currentState.copy(shouldShowHint = shouldShowForYouHintValue)
                            }
                        }
                    },
                    ifRight = { movies ->
                        val models = movies.map(forYouItemUiModelMapper::toUiModel)
                        moviesSuggestionsStack.joinBy(models) { it.tmdbMovieId }
                        updateState { currentState ->
                            currentState.copy(
                                shouldShowHint = shouldShowForYouHintValue
                            )
                        }
                    }
                )
                tvShowsEither.fold(
                    ifLeft = { error ->
                        updateState { currentState ->
                            if (tvShowsSuggestionsStack.isEmpty() || error is SuggestionError.Source) {
                                currentState.copy(
                                    shouldShowHint = false,
                                    suggestedTvShow = toTvShowsSuggestionsState(error)
                                )
                            } else {
                                currentState.copy(shouldShowHint = shouldShowForYouHintValue)
                            }
                        }
                    },
                    ifRight = { tvShows ->
                        val models = tvShows.map(forYouItemUiModelMapper::toUiModel)
                        tvShowsSuggestionsStack.joinBy(models) { it.tmdbTvShowId }
                        updateState { currentState ->
                            currentState.copy(
                                shouldShowHint = shouldShowForYouHintValue
                            )
                        }
                    }
                )
            }.collect()
        }

        viewModelScope.launch {
            moviesSuggestionsStack.collect { stack ->
                val movie = stack.head()
                updateState { currentState ->
                    val suggestedMovie = when (movie) {
                        null -> ForYouState.SuggestedMovie.Loading
                        else -> ForYouState.SuggestedMovie.Data(movie = movie)
                    }
                    currentState.copy(suggestedMovie = suggestedMovie)
                }
            }
        }
        viewModelScope.launch {
            tvShowsSuggestionsStack.collect { stack ->
                val tvShow = stack.head()
                updateState { currentState ->
                    val suggestedTvShow = when (tvShow) {
                        null -> ForYouState.SuggestedTvShow.Loading
                        else -> ForYouState.SuggestedTvShow.Data(tvShow = tvShow)
                    }
                    currentState.copy(suggestedTvShow = suggestedTvShow)
                }
            }
        }
    }

    override fun submit(action: ForYouAction) {
        when (action) {
            is ForYouAction.AddToWatchlist -> onAddToWatchlist(action.itemId)
            is ForYouAction.Dislike -> onDislike(action.itemId)
            is ForYouAction.Like -> onLike(action.itemId)
        }.exhaustive
    }

    private fun onAddToWatchlist(itemId: ForYouItemId) {
        when (itemId) {
            is ForYouItemId.Movie -> {
                moviesSuggestionsStack.pop()
                viewModelScope.launch { addMovieToWatchlist(itemId.tmdbMovieId) }
            }
            is ForYouItemId.TvShow -> {
                tvShowsSuggestionsStack.pop()
                viewModelScope.launch { addTvShowToWatchlist(itemId.tmdbTvShowId) }
            }
        }
    }

    private fun onDislike(itemId: ForYouItemId) {
        when (itemId) {
            is ForYouItemId.Movie -> {
                moviesSuggestionsStack.pop()
                viewModelScope.launch { addMovieToDislikedList(itemId.tmdbMovieId) }
            }
            is ForYouItemId.TvShow -> {
                tvShowsSuggestionsStack.pop()
                viewModelScope.launch { addTvShowToDislikedList(itemId.tmdbTvShowId) }
            }
        }
    }

    private fun onLike(itemId: ForYouItemId) {
        when (itemId) {
            is ForYouItemId.Movie -> {
                moviesSuggestionsStack.pop()
                viewModelScope.launch { addMovieToLikedList(itemId.tmdbMovieId) }
            }
            is ForYouItemId.TvShow -> {
                tvShowsSuggestionsStack.pop()
                viewModelScope.launch { addTvShowToLikedList(itemId.tmdbTvShowId) }
            }
        }
    }

    private fun toMoviesSuggestionsState(error: SuggestionError): ForYouState.SuggestedMovie =
        when (error) {
            is SuggestionError.Source -> {
                val message = networkErrorMapper.toMessage(error.dataError.networkError)
                ForYouState.SuggestedMovie.Error(message)
            }
            is SuggestionError.NoSuggestions -> ForYouState.SuggestedMovie.NoSuggestions
        }

    private fun toTvShowsSuggestionsState(error: SuggestionError): ForYouState.SuggestedTvShow =
        when (error) {
            is SuggestionError.Source -> {
                val message = networkErrorMapper.toMessage(error.dataError.networkError)
                ForYouState.SuggestedTvShow.Error(message)
            }
            is SuggestionError.NoSuggestions -> ForYouState.SuggestedTvShow.NoSuggestions
        }
}

internal operator fun StateFlow<FixedSizeStack<ForYouMovieUiModel>>.contains(movie: Movie) =
    movie.tmdbId in value.all().map { model -> model.tmdbMovieId }
