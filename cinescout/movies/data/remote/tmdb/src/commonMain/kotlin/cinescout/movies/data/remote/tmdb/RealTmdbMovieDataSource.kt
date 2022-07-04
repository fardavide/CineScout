package cinescout.movies.data.remote.tmdb

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.movies.data.remote.TmdbRemoteMovieDataSource
import cinescout.movies.data.remote.tmdb.mapper.TmdbMovieMapper
import cinescout.movies.data.remote.tmdb.service.TmdbMovieService
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId

internal class RealTmdbMovieDataSource(
    private val movieMapper: TmdbMovieMapper,
    private val service: TmdbMovieService
) : TmdbRemoteMovieDataSource {

    override suspend fun getMovie(id: TmdbMovieId): Either<NetworkError, Movie> =
        service.getMovie(id).map { tmdbMovie -> movieMapper.toMovie(tmdbMovie) }

    override suspend fun postRating(movie: Movie, rating: Rating) {
    }

    override suspend fun postWatchlist(movie: Movie) {
    }
}
