package cinescout.watchlist.domain.usecase

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.watchlist.domain.model.WatchlistStoreKey
import cinescout.watchlist.domain.store.WatchlistIdsStore
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreWriteRequest

interface RemoveFromWatchlist {

    suspend operator fun invoke(id: TmdbScreenplayId): Either<NetworkError, Unit>
}

@Factory
internal class RealRemoveFromWatchlist(
    private val watchlistIdsStore: WatchlistIdsStore
) : RemoveFromWatchlist {

    override suspend operator fun invoke(id: TmdbScreenplayId): Either<NetworkError, Unit> =
        watchlistIdsStore.write(StoreWriteRequest.of(WatchlistStoreKey.Write.Remove(id), emptyList()))
}

class FakeRemoveFromWatchlist : RemoveFromWatchlist {

    override suspend operator fun invoke(id: TmdbScreenplayId): Either<NetworkError, Unit> =
        throw NotImplementedError()
}
