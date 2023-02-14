package cinescout.lists.presentation.model

import cinescout.movies.domain.model.TmdbMovieId
import cinescout.tvshows.domain.model.TmdbTvShowId

sealed interface ListItemUiModel {
    val personalRating: String?
    val posterUrl: String?
    val rating: String
    val title: String
    val tmdbIdValue: Int

    data class Movie(
        override val personalRating: String?,
        override val posterUrl: String?,
        override val rating: String,
        override val title: String,
        val tmdbId: TmdbMovieId
    ) : ListItemUiModel {

        override val tmdbIdValue: Int
            get() = tmdbId.value
    }

    data class TvShow(
        override val personalRating: String?,
        override val posterUrl: String?,
        override val rating: String,
        override val title: String,
        val tmdbId: TmdbTvShowId
    ) : ListItemUiModel {

        override val tmdbIdValue: Int
            get() = tmdbId.value
    }
}
