package cinescout.movies.data.remote.tmdb

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.movies.data.remote.TmdbRemoteMovieDataSource
import cinescout.movies.data.remote.tmdb.mapper.TmdbMovieMapper
import cinescout.movies.data.remote.tmdb.model.PostRating
import cinescout.movies.data.remote.tmdb.service.TmdbMovieService
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithRating
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId

internal class RealTmdbMovieDataSource(
    private val movieMapper: TmdbMovieMapper,
    private val movieService: TmdbMovieService
) : TmdbRemoteMovieDataSource {

    override suspend fun discoverMovies(params: DiscoverMoviesParams): Either<NetworkError, List<Movie>> =
        movieService.discoverMovies(params).map { response -> movieMapper.toMovies(response.tmdbMovies()) }

    override suspend fun getMovie(id: TmdbMovieId): Either<NetworkError, Movie> =
        movieService.getMovie(id).map { tmdbMovie -> movieMapper.toMovie(tmdbMovie) }

    override suspend fun getRatedMovies(): Either<NetworkError, List<MovieWithRating>> =
        movieService.getRatedMovies().map { response -> movieMapper.toMoviesWithRating(response) }

    override suspend fun postRating(movie: Movie, rating: Rating): Either<NetworkError, Unit> =
        movieService.postRating(movie.tmdbId, PostRating.Request(rating.value))

    override suspend fun postWatchlist(movie: Movie) {
    }
}
