package cinescout.search.presentation.model

import cinescout.movies.domain.model.TmdbMovieId
import cinescout.tvshows.domain.model.TmdbTvShowId

sealed interface SearchLikedItemId {

    data class Movie(val tmdbMovieId: TmdbMovieId) : SearchLikedItemId
    data class TvShow(val tmdbTvShowId: TmdbTvShowId) : SearchLikedItemId
}

internal val SearchLikedItemId.value: Int
    get() = when (this) {
        is SearchLikedItemId.Movie -> tmdbMovieId.value
        is SearchLikedItemId.TvShow -> tmdbTvShowId.value
    }
