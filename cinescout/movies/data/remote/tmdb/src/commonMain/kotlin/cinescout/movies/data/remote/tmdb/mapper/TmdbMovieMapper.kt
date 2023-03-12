package cinescout.movies.data.remote.tmdb.mapper

import arrow.core.Option
import arrow.core.valueOr
import cinescout.movies.data.remote.model.TmdbMovie
import cinescout.movies.data.remote.tmdb.model.GetMovieDetails
import cinescout.movies.data.remote.tmdb.model.GetMovieWatchlist
import cinescout.movies.data.remote.tmdb.model.GetRatedMovies
import cinescout.movies.domain.model.MovieWithDetails
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.screenplay.domain.model.Genre
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.TmdbBackdropImage
import cinescout.screenplay.domain.model.TmdbGenreId
import cinescout.screenplay.domain.model.TmdbPosterImage
import cinescout.screenplay.domain.model.getOrThrow
import org.koin.core.annotation.Factory
import kotlin.math.roundToInt

@Factory
internal class TmdbMovieMapper {

    fun toMovie(tmdbMovie: TmdbMovie) = Movie(
        backdropImage = Option.fromNullable(tmdbMovie.backdropPath).map(::TmdbBackdropImage),
        overview = tmdbMovie.overview,
        posterImage = Option.fromNullable(tmdbMovie.posterPath).map(::TmdbPosterImage),
        rating = PublicRating(
            voteCount = tmdbMovie.voteCount,
            average = Rating.of(tmdbMovie.voteAverage).getOrThrow()
        ),
        releaseDate = Option.fromNullable(tmdbMovie.releaseDate),
        title = tmdbMovie.title,
        tmdbId = tmdbMovie.id
    )

    fun toMovieWithDetails(response: GetMovieDetails.Response) = MovieWithDetails(
        movie = toMovie(response),
        genres = response.genres.map { genre -> Genre(id = TmdbGenreId(genre.id), name = genre.name) }
    )

    fun toMovie(response: GetMovieDetails.Response) = Movie(
        backdropImage = Option.fromNullable(response.backdropPath).map(::TmdbBackdropImage),
        overview = response.overview,
        posterImage = Option.fromNullable(response.posterPath).map(::TmdbPosterImage),
        rating = PublicRating(
            voteCount = response.voteCount,
            average = Rating.of(response.voteAverage).getOrThrow()
        ),
        releaseDate = Option.fromNullable(response.releaseDate),
        title = response.title,
        tmdbId = response.id
    )

    fun toMovies(tmdbMovies: List<TmdbMovie>): List<Movie> = tmdbMovies.map(::toMovie)

    fun toMovies(response: GetMovieWatchlist.Response): List<Movie> {
        return response.results.map { pageResult ->
            toMovie(pageResult.toTmdbMovie())
        }
    }

    fun toMoviesWithRating(response: GetRatedMovies.Response): List<MovieWithPersonalRating> {
        return response.results.map { pageResult ->
            MovieWithPersonalRating(
                movie = toMovie(pageResult.toTmdbMovie()),
                personalRating = Rating.of(pageResult.rating.roundToInt())
                    .valueOr { throw IllegalStateException("Invalid rating: $it") }
            )
        }
    }
}
