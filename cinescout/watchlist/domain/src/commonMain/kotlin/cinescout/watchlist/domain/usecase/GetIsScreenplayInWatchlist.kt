package cinescout.watchlist.domain.usecase

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.store5.ext.filterData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
class GetIsScreenplayInWatchlist(
    private val getWatchlistIds: GetWatchlistIds
) {

    operator fun invoke(
        screenplayId: TmdbScreenplayId,
        refresh: Boolean
    ): Flow<Either<NetworkError, Boolean>> =
        getWatchlistIds(type = ScreenplayType.All, refresh = refresh).filterData()
            .map { either -> either.map { ids -> screenplayId in ids.map { it.tmdb } } }
}
