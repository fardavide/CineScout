package cinescout.movies.data.remote.tmdb.mapper

import arrow.core.valueOr
import cinescout.movies.data.remote.model.TmdbMovie
import cinescout.movies.data.remote.tmdb.model.GetMovieDetails
import cinescout.movies.data.remote.tmdb.model.GetMovieWatchlist
import cinescout.movies.data.remote.tmdb.model.GetRatedMovies
import cinescout.movies.domain.model.MovieWithDetails
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.screenplay.data.mapper.ScreenplayMapper
import cinescout.screenplay.domain.model.Genre
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.TmdbGenreId
import org.koin.core.annotation.Factory
import kotlin.math.roundToInt

@Factory
internal class TmdbMovieMapper(
    private val screenplayMapper: ScreenplayMapper
) {

    fun toMovie(tmdbMovie: TmdbMovie) = screenplayMapper.toMovie(
        backdropPath = tmdbMovie.backdropPath,
        overview = tmdbMovie.overview,
        posterPath = tmdbMovie.posterPath,
        voteAverage = tmdbMovie.voteAverage,
        voteCount = tmdbMovie.voteCount,
        releaseDate = tmdbMovie.releaseDate,
        title = tmdbMovie.title,
        tmdbId = tmdbMovie.id
    )

    fun toMovieWithDetails(response: GetMovieDetails.Response) = MovieWithDetails(
        movie = toMovie(response),
        genres = response.genres.map { genre -> Genre(id = TmdbGenreId(genre.id), name = genre.name) }
    )

    fun toMovie(response: GetMovieDetails.Response) = screenplayMapper.toMovie(
        backdropPath = response.backdropPath,
        overview = response.overview,
        posterPath = response.posterPath,
        voteAverage = response.voteAverage,
        voteCount = response.voteCount,
        releaseDate = response.releaseDate,
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
