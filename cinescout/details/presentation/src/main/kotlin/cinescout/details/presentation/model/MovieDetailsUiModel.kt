package cinescout.details.presentation.model

import arrow.core.Option
import arrow.core.none
import arrow.core.some
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId

data class MovieDetailsUiModel(
    val backdropUrl: String?,
    val isInWatchlist: Boolean,
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

        sealed class Personal(open val rating: Option<Rating>) {

            data class Rated(override val rating: Option<Rating>, val stringValue: String) : Personal(rating) {
                constructor(rating: Rating, stringValue: String): this(rating.some(), stringValue)
            }
            object NotRated : Personal(none())
        }
    }
}
