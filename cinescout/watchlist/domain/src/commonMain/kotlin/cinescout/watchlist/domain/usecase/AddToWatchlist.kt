package cinescout.watchlist.domain.usecase

import arrow.core.Either
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.watchlist.domain.model.WatchlistStoreKey
import cinescout.watchlist.domain.store.WatchlistIdsStore
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreWriteRequest

interface AddToWatchlist {

    suspend operator fun invoke(id: TmdbScreenplayId): Either<NetworkError, Unit>
}

@Factory
internal class RealAddToWatchlist(
    private val watchlistIdsStore: WatchlistIdsStore
) : AddToWatchlist {

    override suspend operator fun invoke(id: TmdbScreenplayId): Either<NetworkError, Unit> =
        watchlistIdsStore.write(StoreWriteRequest.of(WatchlistStoreKey.Write.Add(id), emptyList()))
}

class FakeAddToWatchlist : AddToWatchlist {

    override suspend operator fun invoke(id: TmdbScreenplayId): Either<NetworkError, Unit> = Unit.right()
}
