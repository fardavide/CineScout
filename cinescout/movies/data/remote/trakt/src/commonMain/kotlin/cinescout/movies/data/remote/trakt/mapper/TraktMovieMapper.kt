package cinescout.movies.data.remote.trakt.mapper

import arrow.core.valueOr
import cinescout.movies.data.remote.model.TraktPersonalMovieRating
import cinescout.movies.data.remote.trakt.model.GetRatings
import cinescout.movies.domain.model.Rating
import kotlin.math.roundToInt

internal class TraktMovieMapper {

    fun toMovieRating(movie: GetRatings.Result.Movie) = TraktPersonalMovieRating(
        tmdbId = movie.movie.ids.tmdb,
        rating = Rating.of(movie.rating.roundToInt())
            .valueOr { throw IllegalStateException("Invalid rating: $it") }
    )

    fun toMovieRatings(movies: List<GetRatings.Result.Movie>): List<TraktPersonalMovieRating> =
        movies.map(::toMovieRating)
}
