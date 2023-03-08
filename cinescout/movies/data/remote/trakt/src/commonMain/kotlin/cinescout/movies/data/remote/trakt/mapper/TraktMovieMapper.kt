package cinescout.movies.data.remote.trakt.mapper

import arrow.core.valueOr
import cinescout.movies.data.remote.model.TraktPersonalMovieRating
import cinescout.movies.data.remote.trakt.model.GetRatings
import cinescout.screenplay.domain.model.Rating
import org.koin.core.annotation.Factory
import kotlin.math.roundToInt

@Factory
internal class TraktMovieMapper {

    fun toMovieRating(movie: GetRatings.Result.Movie) = TraktPersonalMovieRating(
        tmdbId = movie.movie.ids.tmdb,
        rating = Rating.of(movie.rating.roundToInt())
            .valueOr { throw IllegalStateException("Invalid rating: $it") }
    )
}
