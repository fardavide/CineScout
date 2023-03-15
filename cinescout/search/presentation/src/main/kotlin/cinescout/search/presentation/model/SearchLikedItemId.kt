package cinescout.search.presentation.model

import cinescout.screenplay.domain.model.TmdbScreenplayId

sealed interface SearchLikedItemId {

    data class Movie(val tmdbMovieId: TmdbScreenplayId.Movie) : SearchLikedItemId
    data class TvShow(val tmdbTvShowId: TmdbScreenplayId.TvShow) : SearchLikedItemId
}

fun SearchLikedItemId.toScreenplayId() = when (this) {
    is SearchLikedItemId.Movie -> tmdbMovieId
    is SearchLikedItemId.TvShow -> tmdbTvShowId
}

internal val SearchLikedItemId.value: Int
    get() = when (this) {
        is SearchLikedItemId.Movie -> tmdbMovieId.value
        is SearchLikedItemId.TvShow -> tmdbTvShowId.value
    }
