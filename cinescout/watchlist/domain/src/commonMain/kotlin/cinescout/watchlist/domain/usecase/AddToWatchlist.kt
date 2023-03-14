package cinescout.watchlist.domain.usecase

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.watchlist.domain.WatchlistIdsStore
import cinescout.watchlist.domain.model.WatchlistStoreKey
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreWriteRequest

@Factory
class AddToWatchlist(
    private val watchlistIdsStore: WatchlistIdsStore
) {

    suspend operator fun invoke(id: TmdbScreenplayId): Either<NetworkError, Unit> =
        watchlistIdsStore.write(StoreWriteRequest.of(WatchlistStoreKey.Write.Add(id), emptyList()))
}
