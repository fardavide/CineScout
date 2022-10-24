package cinescout.suggestions.presentation.reducer

import arrow.core.NonEmptyList
import cinescout.common.model.SuggestionError
import cinescout.suggestions.presentation.model.ForYouAction
import cinescout.suggestions.presentation.model.ForYouEvent
import cinescout.suggestions.presentation.model.ForYouItemId
import cinescout.suggestions.presentation.model.ForYouMovieUiModel
import cinescout.suggestions.presentation.model.ForYouOperation
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.model.ForYouTvShowUiModel
import cinescout.suggestions.presentation.model.ForYouType
import cinescout.suggestions.presentation.util.joinBy
import cinescout.suggestions.presentation.util.pop
import cinescout.utils.android.Reducer

internal class ForYouReducer : Reducer<ForYouState, ForYouOperation> {

    override fun ForYouState.reduce(operation: ForYouOperation): ForYouState =
        when (operation) {
            is ForYouAction.AddToWatchlist -> onAddToWatchlist(
                currentState = this,
                itemId = operation.itemId
            )
            is ForYouAction.Dislike -> onDislike(
                currentState = this,
                itemId = operation.itemId
            )
            is ForYouAction.Like -> onLike(
                currentState = this,
                itemId = operation.itemId
            )
            is ForYouAction.SelectForYouType -> onSelectForYouType(
                currentState = this,
                forYouType = operation.forYouType
            )
            is ForYouEvent.SuggestedMoviesError -> onSuggestedMoviesError(
                currentState = this,
                error = operation.error,
                shouldShowHint = operation.shouldShowHint,
                toErrorState = operation.toErrorState
            )
            is ForYouEvent.SuggestedMoviesReceived -> onSuggestedMoviesReceived(
                currentState = this,
                movies = operation.movies,
                shouldShowHint = operation.shouldShowHint
            )
            is ForYouEvent.SuggestedTvShowsError -> onSuggestedTvShowsError(
                currentState = this,
                error = operation.error,
                shouldShowHint = operation.shouldShowHint,
                toErrorState = operation.toErrorState
            )
            is ForYouEvent.SuggestedTvShowsReceived -> onSuggestedTvShowsReceived(
                currentState = this,
                tvShows = operation.tvShows,
                shouldShowHint = operation.shouldShowHint
            )
        }

    private fun onAddToWatchlist(
        currentState: ForYouState,
        itemId: ForYouItemId
    ): ForYouState = when (itemId) {
        is ForYouItemId.Movie -> currentState.popMovie()
        is ForYouItemId.TvShow -> currentState.popTvShow()
    }

    private fun onDislike(
        currentState: ForYouState,
        itemId: ForYouItemId
    ): ForYouState = when (itemId) {
        is ForYouItemId.Movie -> currentState.popMovie()
        is ForYouItemId.TvShow -> currentState.popTvShow()
    }

    private fun onLike(
        currentState: ForYouState,
        itemId: ForYouItemId
    ): ForYouState = when (itemId) {
        is ForYouItemId.Movie -> currentState.popMovie()
        is ForYouItemId.TvShow -> currentState.popTvShow()
    }

    private fun onSelectForYouType(
        currentState: ForYouState,
        forYouType: ForYouType
    ): ForYouState = currentState.copy(
        suggestedItem = when (forYouType) {
            ForYouType.Movies -> when (val movie = currentState.moviesStack.head()) {
                null -> ForYouState.SuggestedItem.NoSuggestedMovies
                else -> ForYouState.SuggestedItem.Movie(movie)
            }
            ForYouType.TvShows -> when (val tvShow = currentState.tvShowsStack.head()) {
                null -> ForYouState.SuggestedItem.NoSuggestedTvShows
                else -> ForYouState.SuggestedItem.TvShow(tvShow)
            }
        },
        type = forYouType
    )

    private fun onSuggestedMoviesError(
        currentState: ForYouState,
        error: SuggestionError,
        shouldShowHint: Boolean,
        toErrorState: (SuggestionError) -> ForYouState.SuggestedItem
    ): ForYouState = when {
        currentState.type != ForYouType.Movies -> currentState
        currentState.moviesStack.isEmpty() || error is SuggestionError.NoSuggestions -> currentState.copy(
            shouldShowHint = false,
            suggestedItem = toErrorState(error)
        )
        else -> currentState.copy(shouldShowHint = shouldShowHint)
    }

    private fun onSuggestedMoviesReceived(
        currentState: ForYouState,
        movies: NonEmptyList<ForYouMovieUiModel>,
        shouldShowHint: Boolean
    ): ForYouState {
        val moviesStack = currentState.moviesStack.joinBy(movies) { it.tmdbMovieId }
        return when (currentState.type) {
            ForYouType.Movies -> currentState.copy(
                moviesStack = moviesStack,
                shouldShowHint = shouldShowHint,
                suggestedItem = ForYouState.SuggestedItem.Movie(movies.head)
            )
            else -> currentState.copy(moviesStack = moviesStack)
        }
    }

    private fun onSuggestedTvShowsError(
        currentState: ForYouState,
        error: SuggestionError,
        shouldShowHint: Boolean,
        toErrorState: (SuggestionError) -> ForYouState.SuggestedItem
    ): ForYouState = when {
        currentState.type != ForYouType.TvShows -> currentState
        currentState.tvShowsStack.isEmpty() || error is SuggestionError.NoSuggestions -> currentState.copy(
            shouldShowHint = false,
            suggestedItem = toErrorState(error)
        )
        else -> currentState.copy(shouldShowHint = shouldShowHint)
    }

    private fun onSuggestedTvShowsReceived(
        currentState: ForYouState,
        tvShows: NonEmptyList<ForYouTvShowUiModel>,
        shouldShowHint: Boolean
    ): ForYouState {
        val tvShowsStack = currentState.tvShowsStack.joinBy(tvShows) { it.tmdbTvShowId }
        return when (currentState.type) {
            ForYouType.TvShows -> currentState.copy(
                tvShowsStack = tvShowsStack,
                shouldShowHint = shouldShowHint,
                suggestedItem = ForYouState.SuggestedItem.TvShow(tvShows.head)
            )
            else -> currentState.copy(tvShowsStack = tvShowsStack)
        }
    }

    private fun ForYouState.popMovie(): ForYouState {
        val (stack, _) = moviesStack.pop()
        val movie = stack.head()
        return copy(
            moviesStack = stack,
            suggestedItem = when (movie) {
                null -> ForYouState.SuggestedItem.NoSuggestedMovies
                else -> ForYouState.SuggestedItem.Movie(movie)
            }
        )
    }

    private fun ForYouState.popTvShow(): ForYouState {
        val (stack, _) = tvShowsStack.pop()
        val tvShow = stack.head()
        return copy(
            tvShowsStack = stack,
            suggestedItem = when (tvShow) {
                null -> ForYouState.SuggestedItem.NoSuggestedTvShows
                else -> ForYouState.SuggestedItem.TvShow(tvShow)
            }
        )
    }
}
