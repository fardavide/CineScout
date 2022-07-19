package cinescout.movies.data.remote.trakt.mapper

import arrow.core.valueOr
import cinescout.movies.data.remote.trakt.model.GetRatings
import cinescout.movies.domain.model.MovieRating
import cinescout.movies.domain.model.Rating
import kotlin.math.roundToInt

internal class TraktMovieMapper {

    fun toMovieRating(movie: GetRatings.Result.Movie) = MovieRating(
        tmdbId = movie.movie.ids.tmdb,
        rating = Rating.of(movie.rating.roundToInt())
            .valueOr { throw IllegalStateException("Invalid rating: $it") }
    )
}
