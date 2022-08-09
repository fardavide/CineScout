package cinescout.movies.data.remote.tmdb.mapper

import arrow.core.Option
import arrow.core.valueOr
import cinescout.movies.data.remote.model.TmdbMovie
import cinescout.movies.data.remote.tmdb.model.GetMovieDetails
import cinescout.movies.data.remote.tmdb.model.GetRatedMovies
import cinescout.movies.domain.model.Genre
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieRating
import cinescout.movies.domain.model.MovieWithDetails
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbBackdropImage
import cinescout.movies.domain.model.TmdbGenreId
import cinescout.movies.domain.model.TmdbPosterImage
import cinescout.movies.domain.model.getOrThrow
import cinescout.utils.kotlin.nonEmptyUnsafe
import kotlin.math.roundToInt

class TmdbMovieMapper {

    fun toMovie(tmdbMovie: TmdbMovie) = Movie(
        backdropImage = Option.fromNullable(tmdbMovie.backdropPath).map(::TmdbBackdropImage),
        posterImage = Option.fromNullable(tmdbMovie.posterPath).map(::TmdbPosterImage),
        rating = MovieRating(
            voteCount = tmdbMovie.voteCount,
            average = Rating.of(tmdbMovie.voteAverage).getOrThrow()
        ),
        releaseDate = tmdbMovie.releaseDate,
        title = tmdbMovie.title,
        tmdbId = tmdbMovie.id
    )

    fun toMovieWithDetails(response: GetMovieDetails.Response) = MovieWithDetails(
        movie = toMovie(response),
        genres = response.genres.nonEmptyUnsafe().map { genre -> Genre(id = TmdbGenreId(genre.id), name = genre.name) }
    )

    fun toMovie(response: GetMovieDetails.Response) = Movie(
        backdropImage = Option.fromNullable(response.backdropPath).map(::TmdbBackdropImage),
        posterImage = Option.fromNullable(response.posterPath).map(::TmdbPosterImage),
        rating = MovieRating(
            voteCount = response.voteCount,
            average = Rating.of(response.voteAverage).getOrThrow()
        ),
        releaseDate = response.releaseDate,
        title = response.title,
        tmdbId = response.id
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
