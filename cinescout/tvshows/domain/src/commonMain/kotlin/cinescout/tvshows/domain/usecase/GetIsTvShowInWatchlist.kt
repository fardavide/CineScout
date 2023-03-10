package cinescout.tvshows.domain.usecase

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.store5.ext.filterData
import cinescout.tvshows.domain.model.TmdbTvShowId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
class GetIsTvShowInWatchlist(
    private val getAllWatchlistTvShows: GetAllWatchlistTvShows
) {

    operator fun invoke(id: TmdbTvShowId, refresh: Boolean = true): Flow<Either<NetworkError, Boolean>> =
        getAllWatchlistTvShows(refresh).filterData().map { either ->
            either.map { tvShows ->
                tvShows.any { it.tmdbId == id }
            }
        }
}
