package cinescout.suggestions.presentation.model

import arrow.core.NonEmptyList
import cinescout.common.model.SuggestionError
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.tvshows.domain.model.TmdbTvShowId

sealed interface ForYouOperation

sealed interface ForYouAction : ForYouOperation {

    data class AddToWatchlist(val itemId: ForYouItemId) : ForYouAction

    data class Dislike(val itemId: ForYouItemId) : ForYouAction

    data class Like(val itemId: ForYouItemId) : ForYouAction

    class SelectForYouType(val forYouType: ForYouType) : ForYouAction

    companion object {

        fun AddToWatchlist(tmdbMovieId: TmdbMovieId) = AddToWatchlist(ForYouItemId.Movie(tmdbMovieId))
        fun AddToWatchlist(tmdbTvShowId: TmdbTvShowId) = AddToWatchlist(ForYouItemId.TvShow(tmdbTvShowId))
        fun Dislike(tmdbMovieId: TmdbMovieId) = Dislike(ForYouItemId.Movie(tmdbMovieId))
        fun Dislike(tmdbTvShowId: TmdbTvShowId) = Dislike(ForYouItemId.TvShow(tmdbTvShowId))
        fun Like(tmdbMovieId: TmdbMovieId) = Like(ForYouItemId.Movie(tmdbMovieId))
        fun Like(tmdbTvShowId: TmdbTvShowId) = Like(ForYouItemId.TvShow(tmdbTvShowId))
    }
}

internal sealed interface ForYouEvent : ForYouOperation {

    data class SuggestedMoviesError(
        val error: SuggestionError,
        val shouldShowHint: Boolean,
        val toErrorState: (SuggestionError) -> ForYouState.SuggestedItem
    ) : ForYouEvent

    data class SuggestedMoviesReceived(
        val movies: NonEmptyList<ForYouMovieUiModel>,
        val shouldShowHint: Boolean
    ) : ForYouEvent

    data class SuggestedTvShowsError(
        val error: SuggestionError,
        val shouldShowHint: Boolean,
        val toErrorState: (SuggestionError) -> ForYouState.SuggestedItem
    ) : ForYouEvent

    data class SuggestedTvShowsReceived(
        val tvShows: NonEmptyList<ForYouTvShowUiModel>,
        val shouldShowHint: Boolean
    ) : ForYouEvent
}
