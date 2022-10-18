package cinescout.tvshows.data.remote.trakt

import arrow.core.Either
import cinescout.auth.trakt.domain.usecase.CallWithTraktAccount
import cinescout.common.model.Rating
import cinescout.model.NetworkOperation
import cinescout.tvshows.data.remote.TraktRemoteTvShowDataSource
import cinescout.tvshows.data.remote.model.TraktPersonalTvShowRating
import cinescout.tvshows.data.remote.trakt.mapper.TraktTvShowMapper
import cinescout.tvshows.data.remote.trakt.service.TraktTvShowService
import cinescout.tvshows.domain.model.TmdbTvShowId
import store.PagedData
import store.Paging

internal class RealTraktTvShowDataSource(
    private val callWithTraktAccount: CallWithTraktAccount,
    private val service: TraktTvShowService,
    private val tvShowMapper: TraktTvShowMapper
) : TraktRemoteTvShowDataSource {

    override suspend fun getRatedTvShows(
        page: Int
    ): Either<NetworkOperation, PagedData.Remote<TraktPersonalTvShowRating, Paging.Page.SingleSource>> =
        callWithTraktAccount {
            service.getRatedTvShows(page).map { pagedData ->
                pagedData.map { tvShow ->
                    tvShowMapper.toTvShowRating(tvShow)
                }
            }
        }

    override suspend fun getWatchlistTvShows(
        page: Int
    ): Either<NetworkOperation, PagedData.Remote<TmdbTvShowId, Paging.Page.SingleSource>> =
        callWithTraktAccount {
            service.getWatchlistTvShows(page).map { pagedData ->
                pagedData.map { tvShow ->
                    tvShow.tvShow.ids.tmdb
                }
            }
        }

    override suspend fun postRating(tvShowId: TmdbTvShowId, rating: Rating): Either<NetworkOperation, Unit> =
        callWithTraktAccount {
            service.postRating(tvShowId, rating)
        }

    override suspend fun postAddToWatchlist(tvShowId: TmdbTvShowId): Either<NetworkOperation, Unit> =
        callWithTraktAccount {
            service.postAddToWatchlist(tvShowId)
        }

    override suspend fun postRemoveFromWatchlist(tvShowId: TmdbTvShowId): Either<NetworkOperation, Unit> =
        callWithTraktAccount {
            service.postRemoveFromWatchlist(tvShowId)
        }
}
