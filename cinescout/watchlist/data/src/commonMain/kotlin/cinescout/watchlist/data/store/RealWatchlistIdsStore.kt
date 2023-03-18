package cinescout.watchlist.data.store

import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.store5.EitherFetcher
import cinescout.store5.EitherUpdater
import cinescout.store5.MutableStore5
import cinescout.store5.Store5Builder
import cinescout.store5.empty
import cinescout.watchlist.data.datasource.LocalWatchlistDataSource
import cinescout.watchlist.data.datasource.RemoteWatchlistDataSource
import cinescout.watchlist.domain.model.WatchlistStoreKey
import cinescout.watchlist.domain.store.WatchlistIdsStore
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.Bookkeeper
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [WatchlistIdsStore::class])
internal class RealWatchlistIdsStore(
    private val localDataSource: LocalWatchlistDataSource,
    private val remoteDataSource: RemoteWatchlistDataSource
) : WatchlistIdsStore,
    MutableStore5<WatchlistStoreKey, List<TmdbScreenplayId>, Unit> by Store5Builder
        .from<WatchlistStoreKey, List<TmdbScreenplayId>>(
            fetcher = EitherFetcher.ofOperation { key ->
                require(key is WatchlistStoreKey.Read) { "Write keys are not supported for fetcher" }
                remoteDataSource.getAllWatchlistIds(key.type)
            },
            sourceOfTruth = SourceOfTruth.of(
                reader = { key ->
                    require(key is WatchlistStoreKey.Read) { "Write keys are not supported for reader" }
                    localDataSource.findWatchlistIds(key.type)
                },
                writer = { _, ids -> localDataSource.updateWatchlistIds(ids) },
                delete = { _ -> localDataSource.deleteAllWatchlistIds() },
                deleteAll = { localDataSource.deleteAllWatchlistIds() }
            )
        )
        .buildMutable(
            updater = EitherUpdater.byOperation({ key: WatchlistStoreKey, _ ->
                when (key) {
                    is WatchlistStoreKey.Read -> error("Read keys are not supported for updater")
                    is WatchlistStoreKey.Write.Add -> {
                        remoteDataSource.postAddToWatchlist(key.id)
                    }
                    is WatchlistStoreKey.Write.Remove -> {
                        remoteDataSource.postRemoveFromWatchlist(key.id)
                    }
                }
            }),
            bookkeeper = Bookkeeper.empty()
        )
