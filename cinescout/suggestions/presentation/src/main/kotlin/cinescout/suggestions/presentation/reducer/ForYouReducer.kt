package cinescout.suggestions.presentation.reducer

import arrow.core.NonEmptyList
import cinescout.common.model.SuggestionError
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.suggestions.presentation.model.ForYouAction
import cinescout.suggestions.presentation.model.ForYouEvent
import cinescout.suggestions.presentation.model.ForYouOperation
import cinescout.suggestions.presentation.model.ForYouScreenplayUiModel
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.model.ForYouType
import cinescout.suggestions.presentation.util.joinBy
import cinescout.suggestions.presentation.util.pop
import cinescout.utils.android.Reducer
import org.koin.core.annotation.Factory

@Factory
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
                toErrorState = operation.toErrorState
            )
            is ForYouEvent.SuggestedMoviesReceived -> onSuggestedMoviesReceived(
                currentState = this,
                movies = operation.movies
            )
            is ForYouEvent.SuggestedTvShowsError -> onSuggestedTvShowsError(
                currentState = this,
                error = operation.error,
                toErrorState = operation.toErrorState
            )
            is ForYouEvent.SuggestedTvShowsReceived -> onSuggestedTvShowsReceived(
                currentState = this,
                tvShows = operation.tvShows
            )
        }

    private fun onAddToWatchlist(
        currentState: ForYouState,
        itemId: TmdbScreenplayId
    ): ForYouState = when (itemId) {
        is TmdbScreenplayId.Movie -> currentState.popMovie()
        is TmdbScreenplayId.TvShow -> currentState.popTvShow()
    }

    private fun onDislike(
        currentState: ForYouState,
        itemId: TmdbScreenplayId
    ): ForYouState = when (itemId) {
        is TmdbScreenplayId.Movie -> currentState.popMovie()
        is TmdbScreenplayId.TvShow -> currentState.popTvShow()
    }

    private fun onLike(
        currentState: ForYouState,
        itemId: TmdbScreenplayId
    ): ForYouState = when (itemId) {
        is TmdbScreenplayId.Movie -> currentState.popMovie()
        is TmdbScreenplayId.TvShow -> currentState.popTvShow()
    }

    private fun onSelectForYouType(
        currentState: ForYouState,
        forYouType: ForYouType
    ): ForYouState = currentState.copy(
        suggestedItem = when (forYouType) {
            ForYouType.Movies -> when (val movie = currentState.moviesStack.head()) {
                null -> ForYouState.SuggestedItem.NoSuggestedMovies
                else -> ForYouState.SuggestedItem.Screenplay(movie)
            }
            ForYouType.TvShows -> when (val tvShow = currentState.tvShowsStack.head()) {
                null -> ForYouState.SuggestedItem.NoSuggestedTvShows
                else -> ForYouState.SuggestedItem.Screenplay(tvShow)
            }
        },
        type = forYouType
    )

    private fun onSuggestedMoviesError(
        currentState: ForYouState,
        error: SuggestionError,
        toErrorState: (SuggestionError) -> ForYouState.SuggestedItem
    ): ForYouState = when {
        currentState.type != ForYouType.Movies -> currentState
        currentState.moviesStack.isEmpty() || error is SuggestionError.NoSuggestions -> currentState.copy(
            suggestedItem = toErrorState(error)
        )
        else -> currentState
    }

    private fun onSuggestedMoviesReceived(
        currentState: ForYouState,
        movies: NonEmptyList<ForYouScreenplayUiModel>
    ): ForYouState {
        val moviesStack = currentState.moviesStack.joinBy(movies) { it.tmdbScreenplayId }
        return when (currentState.type) {
            ForYouType.Movies -> currentState.copy(
                moviesStack = moviesStack,
                suggestedItem = ForYouState.SuggestedItem.Screenplay(movies.head)
            )
            else -> currentState.copy(moviesStack = moviesStack)
        }
    }

    private fun onSuggestedTvShowsError(
        currentState: ForYouState,
        error: SuggestionError,
        toErrorState: (SuggestionError) -> ForYouState.SuggestedItem
    ): ForYouState = when {
        currentState.type != ForYouType.TvShows -> currentState
        currentState.tvShowsStack.isEmpty() || error is SuggestionError.NoSuggestions -> currentState.copy(
            suggestedItem = toErrorState(error)
        )
        else -> currentState
    }

    private fun onSuggestedTvShowsReceived(
        currentState: ForYouState,
        tvShows: NonEmptyList<ForYouScreenplayUiModel>
    ): ForYouState {
        val tvShowsStack = currentState.tvShowsStack.joinBy(tvShows) { it.tmdbScreenplayId }
        return when (currentState.type) {
            ForYouType.TvShows -> currentState.copy(
                tvShowsStack = tvShowsStack,
                suggestedItem = ForYouState.SuggestedItem.Screenplay(tvShows.head)
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
                else -> ForYouState.SuggestedItem.Screenplay(movie)
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
                else -> ForYouState.SuggestedItem.Screenplay(tvShow)
            }
        )
    }
}
