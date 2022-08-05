package cinescout.movies.data.remote.tmdb.mapper

import arrow.core.valueOr
import cinescout.movies.data.remote.model.TmdbMovie
import cinescout.movies.data.remote.tmdb.model.GetRatedMovies
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.Rating
import kotlin.math.roundToInt

class TmdbMovieMapper {

    fun toMovie(tmdbMovie: TmdbMovie) = Movie(
        releaseDate = tmdbMovie.releaseDate,
        title = tmdbMovie.title,
        tmdbId = tmdbMovie.id
    )

    fun toMovies(tmdbMovies: List<TmdbMovie>): List<Movie> =
        tmdbMovies.map(::toMovie)

    fun toMoviesWithRating(response: GetRatedMovies.Response): List<MovieWithPersonalRating> {
        return response.results.map { pageResult ->
            MovieWithPersonalRating(
                movie = toMovie(pageResult.toTmdbMovie()),
                rating = Rating.of(pageResult.rating.roundToInt())
                    .valueOr { throw IllegalStateException("Invalid rating: $it") }
            )
        }
    }
}
