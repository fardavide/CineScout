package cinescout.movies.data.remote.trakt

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.movies.data.remote.TraktRemoteMovieDataSource
import cinescout.movies.data.remote.model.TraktPersonalMovieRating
import cinescout.movies.data.remote.trakt.mapper.TraktMovieMapper
import cinescout.movies.data.remote.trakt.service.TraktMovieService
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId
import store.PagedData
import store.Paging

internal class RealTraktMovieDataSource(
    private val movieMapper: TraktMovieMapper,
    private val service: TraktMovieService
) : TraktRemoteMovieDataSource {

    override suspend fun getRatedMovies(
        page: Int
    ): Either<NetworkError, PagedData.Remote<TraktPersonalMovieRating, Paging.Page.SingleSource>> =
        service.getRatedMovies(page).map { pagedData ->
            pagedData.map { movie ->
                movieMapper.toMovieRating(movie)
            }
        }

    override suspend fun getWatchlistMovies(
        page: Int
    ): Either<NetworkError, PagedData.Remote<TmdbMovieId, Paging.Page.SingleSource>> =
        service.getWatchlistMovies(page).map { pagedData ->
            pagedData.map { movie ->
                movie.movie.ids.tmdb
            }
        }

    override suspend fun postRating(movieId: TmdbMovieId, rating: Rating): Either<NetworkError, Unit> =
        service.postRating(movieId, rating)

    override suspend fun postWatchlist(id: TmdbMovieId): Either<NetworkError, Unit> =
        service.postAddToWatchlist(id)
}
