package cinescout.watchlist.domain.usecase

import arrow.core.Either
import arrow.core.right
import cinescout.CineScoutTestApi
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.watchlist.domain.model.WatchlistStoreKey
import cinescout.watchlist.domain.store.WatchlistIdsStore
import org.koin.core.annotation.Factory

interface ToggleWatchlist {

    suspend operator fun invoke(screenplayIds: ScreenplayIds): Either<NetworkError, Unit>
}

@Factory
internal class RealToggleWatchlist(
    private val addToWatchlist: AddToWatchlist,
    private val watchlistIdsStore: WatchlistIdsStore,
    private val removeFromWatchlist: RemoveFromWatchlist
) : ToggleWatchlist {

    override suspend operator fun invoke(screenplayIds: ScreenplayIds): Either<NetworkError, Unit> {
        val isInWatchlistEither = watchlistIdsStore.get(WatchlistStoreKey.Read(ScreenplayTypeFilter.All))
            .map { list -> screenplayIds in list }
        return isInWatchlistEither.map { isInWatchlist ->
            when (isInWatchlist) {
                true -> removeFromWatchlist(screenplayIds.tmdb)
                false -> addToWatchlist(screenplayIds)
            }
        }
    }
}

@CineScoutTestApi
class FakeToggleWatchlist : ToggleWatchlist {

    override suspend fun invoke(screenplayIds: ScreenplayIds): Either<NetworkError, Unit> = Unit.right()
}
