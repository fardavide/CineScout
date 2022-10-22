package cinescout.suggestions.presentation.model

import cinescout.movies.domain.model.TmdbMovieId
import cinescout.tvshows.domain.model.TmdbTvShowId

sealed interface ForYouAction {

    data class AddToWatchlist(val itemId: ForYouItemId) : ForYouAction

    data class Dislike(val itemId: ForYouItemId) : ForYouAction

    data class Like(val itemId: ForYouItemId) : ForYouAction

    companion object {

        fun AddToWatchlist(tmdbMovieId: TmdbMovieId) = AddToWatchlist(ForYouItemId.Movie(tmdbMovieId))
        fun AddToWatchlist(tmdbTvShowId: TmdbTvShowId) = AddToWatchlist(ForYouItemId.TvShow(tmdbTvShowId))
        fun Dislike(tmdbMovieId: TmdbMovieId) = Dislike(ForYouItemId.Movie(tmdbMovieId))
        fun Dislike(tmdbTvShowId: TmdbTvShowId) = Dislike(ForYouItemId.TvShow(tmdbTvShowId))
        fun Like(tmdbMovieId: TmdbMovieId) = Like(ForYouItemId.Movie(tmdbMovieId))
        fun Like(tmdbTvShowId: TmdbTvShowId) = Like(ForYouItemId.TvShow(tmdbTvShowId))
    }
}
