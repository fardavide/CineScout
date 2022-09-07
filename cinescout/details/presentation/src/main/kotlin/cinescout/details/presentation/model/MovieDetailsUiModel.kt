package cinescout.details.presentation.model

import cinescout.movies.domain.model.TmdbMovieId

data class MovieDetailsUiModel(
    val backdropUrl: String?,
    val posterUrl: String?,
    val ratings: Ratings,
    val releaseDate: String,
    val title: String,
    val tmdbId: TmdbMovieId
) {

    data class Ratings(
        val publicAverage: String,
        val publicCount: String,
        val personal: Personal
    ) {

        sealed interface Personal {

            data class Rated(val rating: String) : Personal
            object NotRated : Personal
        }
    }
}
