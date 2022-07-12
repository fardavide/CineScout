package cinescout.movies.data.remote.tmdb.mapper

import arrow.core.valueOr
import cinescout.movies.data.remote.model.TmdbMovie
import cinescout.movies.data.remote.tmdb.model.GetRatedMovies
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithRating
import cinescout.movies.domain.model.Rating

class TmdbMovieMapper {

    fun toMovie(tmdbMovie: TmdbMovie) = Movie(title = tmdbMovie.title, tmdbId = tmdbMovie.id)

    fun toMoviesWithRating(response: GetRatedMovies.Response): List<MovieWithRating> {
        return response.results.map { pageResult ->
            MovieWithRating(
                movie = toMovie(pageResult.toTmdbMovie()),
                rating = Rating.of(pageResult.rating)
                    .valueOr { throw IllegalStateException("Invalid rating: $it") }
            )
        }
    }
}
