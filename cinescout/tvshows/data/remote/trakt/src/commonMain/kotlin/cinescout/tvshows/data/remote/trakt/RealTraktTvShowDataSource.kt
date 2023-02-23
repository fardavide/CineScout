package cinescout.tvshows.data.remote.trakt

import arrow.core.Either
import cinescout.common.model.Rating
import cinescout.error.NetworkError
import cinescout.tvshows.data.remote.TraktRemoteTvShowDataSource
import cinescout.tvshows.data.remote.model.TraktPersonalTvShowRating
import cinescout.tvshows.data.remote.trakt.mapper.TraktTvShowMapper
import cinescout.tvshows.data.remote.trakt.service.TraktTvShowService
import cinescout.tvshows.domain.model.TmdbTvShowId
import org.koin.core.annotation.Factory
import store.PagedData

@Factory
internal class RealTraktTvShowDataSource(
    private val service: TraktTvShowService,
    private val tvShowMapper: TraktTvShowMapper
) : TraktRemoteTvShowDataSource {

    override suspend fun getRatedTvShows(
        page: Int
    ): Either<NetworkError, PagedData.Remote<TraktPersonalTvShowRating>> =
        service.getRatedTvShows(page).map { pagedData ->
            pagedData.map { tvShow ->
                tvShowMapper.toTvShowRating(tvShow)
            }
        }

    override suspend fun getWatchlistTvShows(
        page: Int
    ): Either<NetworkError, PagedData.Remote<TmdbTvShowId>> =
        service.getWatchlistTvShows(page).map { pagedData ->
            pagedData.map { tvShow ->
                tvShow.tvShow.ids.tmdb
            }
        }

    override suspend fun postRating(tvShowId: TmdbTvShowId, rating: Rating): Either<NetworkError, Unit> =
        service.postRating(tvShowId, rating)

    override suspend fun postAddToWatchlist(tvShowId: TmdbTvShowId): Either<NetworkError, Unit> =
        service.postAddToWatchlist(tvShowId)

    override suspend fun postRemoveFromWatchlist(tvShowId: TmdbTvShowId): Either<NetworkError, Unit> =
        service.postRemoveFromWatchlist(tvShowId)
}
