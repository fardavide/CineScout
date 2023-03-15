package cinescout.watchlist.domain.usecase

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.watchlist.domain.model.WatchlistStoreKey
import cinescout.watchlist.domain.store.WatchlistIdsStore
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreWriteRequest

@Factory
class RemoveFromWatchlist(
    private val watchlistIdsStore: WatchlistIdsStore
) {

    suspend operator fun invoke(id: TmdbScreenplayId): Either<NetworkError, Unit> =
        watchlistIdsStore.write(StoreWriteRequest.of(WatchlistStoreKey.Write.Remove(id), emptyList()))
}
