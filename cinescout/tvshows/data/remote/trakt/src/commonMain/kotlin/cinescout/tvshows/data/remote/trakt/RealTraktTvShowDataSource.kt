package cinescout.tvshows.data.remote.trakt

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.Rating
import cinescout.tvshows.data.remote.TraktRemoteTvShowDataSource
import cinescout.tvshows.data.remote.model.TraktPersonalTvShowRating
import cinescout.tvshows.data.remote.trakt.mapper.TraktTvShowMapper
import cinescout.tvshows.data.remote.trakt.service.TraktTvShowService
import cinescout.tvshows.domain.model.TmdbTvShowId
import org.koin.core.annotation.Factory

@Factory
internal class RealTraktTvShowDataSource(
    private val service: TraktTvShowService,
    private val tvShowMapper: TraktTvShowMapper
) : TraktRemoteTvShowDataSource {

    override suspend fun getRatedTvShows(): Either<NetworkError, List<TraktPersonalTvShowRating>> =
        service.getRatedTvShows().map { list ->
            list.map { tvShow ->
                tvShowMapper.toTvShowRating(tvShow)
            }
        }

    override suspend fun getWatchlistTvShows(): Either<NetworkError, List<TmdbTvShowId>> =
        service.getWatchlistTvShows().map { list ->
            list.map { tvShow ->
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
