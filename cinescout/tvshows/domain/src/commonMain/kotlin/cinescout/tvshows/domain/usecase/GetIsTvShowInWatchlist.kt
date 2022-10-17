package cinescout.tvshows.domain.usecase

import arrow.core.Either
import cinescout.error.DataError
import cinescout.tvshows.domain.model.TmdbTvShowId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import store.Refresh
import kotlin.time.Duration.Companion.minutes

class GetIsTvShowInWatchlist(
    private val getAllWatchlistTvShows: GetAllWatchlistTvShows
) {

    operator fun invoke(
        id: TmdbTvShowId,
        refresh: Refresh = Refresh.IfExpired(validity = 5.minutes)
    ): Flow<Either<DataError, Boolean>> =
        getAllWatchlistTvShows(refresh).map { either ->
            either.map { tvShows ->
                tvShows.data.any { it.tmdbId == id }
            }
        }
}
