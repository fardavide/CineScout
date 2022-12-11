package cinescout.movies.data.remote.trakt

import arrow.core.Either
import cinescout.auth.trakt.domain.usecase.CallWithTraktAccount
import cinescout.common.model.Rating
import cinescout.model.NetworkOperation
import cinescout.movies.data.remote.TraktRemoteMovieDataSource
import cinescout.movies.data.remote.model.TraktPersonalMovieRating
import cinescout.movies.data.remote.trakt.mapper.TraktMovieMapper
import cinescout.movies.data.remote.trakt.service.TraktMovieService
import cinescout.movies.domain.model.TmdbMovieId
import org.koin.core.annotation.Factory
import store.PagedData
import store.Paging

@Factory
internal class RealTraktMovieDataSource(
    private val callWithTraktAccount: CallWithTraktAccount,
    private val movieMapper: TraktMovieMapper,
    private val service: TraktMovieService
) : TraktRemoteMovieDataSource {

    override suspend fun getRatedMovies(
        page: Int
    ): Either<NetworkOperation, PagedData.Remote<TraktPersonalMovieRating, Paging.Page.SingleSource>> =
        callWithTraktAccount {
            service.getRatedMovies(page).map { pagedData ->
                pagedData.map { movie ->
                    movieMapper.toMovieRating(movie)
                }
            }
        }

    override suspend fun getWatchlistMovies(
        page: Int
    ): Either<NetworkOperation, PagedData.Remote<TmdbMovieId, Paging.Page.SingleSource>> =
        callWithTraktAccount {
            service.getWatchlistMovies(page).map { pagedData ->
                pagedData.map { movie ->
                    movie.movie.ids.tmdb
                }
            }
        }

    override suspend fun postRating(movieId: TmdbMovieId, rating: Rating): Either<NetworkOperation, Unit> =
        callWithTraktAccount {
            service.postRating(movieId, rating)
        }

    override suspend fun postAddToWatchlist(movieId: TmdbMovieId): Either<NetworkOperation, Unit> =
        callWithTraktAccount {
            service.postAddToWatchlist(movieId)
        }

    override suspend fun postRemoveFromWatchlist(movieId: TmdbMovieId): Either<NetworkOperation, Unit> =
        callWithTraktAccount {
            service.postRemoveFromWatchlist(movieId)
        }
}
