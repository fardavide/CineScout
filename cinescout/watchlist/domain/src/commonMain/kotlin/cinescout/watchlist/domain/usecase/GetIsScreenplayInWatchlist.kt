package cinescout.watchlist.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.store5.ext.filterData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.jetbrains.annotations.VisibleForTesting
import org.koin.core.annotation.Factory

interface GetIsScreenplayInWatchlist {

    operator fun invoke(screenplayId: TmdbScreenplayId, refresh: Boolean): Flow<Either<NetworkError, Boolean>>
}

@Factory
class RealGetIsScreenplayInWatchlist(
    private val getWatchlistIds: GetWatchlistIds
) : GetIsScreenplayInWatchlist {

    override operator fun invoke(
        screenplayId: TmdbScreenplayId,
        refresh: Boolean
    ): Flow<Either<NetworkError, Boolean>> =
        getWatchlistIds(type = ScreenplayTypeFilter.All, refresh = refresh).filterData()
            .map { either -> either.map { ids -> screenplayId in ids.map { it.tmdb } } }
}

@VisibleForTesting
class FakeGetIsScreenplayInWatchlist(
    private val isInWatchlist: Boolean? = null
) : GetIsScreenplayInWatchlist {

    override operator fun invoke(
        screenplayId: TmdbScreenplayId,
        refresh: Boolean
    ): Flow<Either<NetworkError, Boolean>> = flowOf(isInWatchlist?.right() ?: NetworkError.Unknown.left())
}
