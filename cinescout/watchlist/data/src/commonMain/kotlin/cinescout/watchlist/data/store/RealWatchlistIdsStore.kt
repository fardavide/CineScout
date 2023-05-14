package cinescout.watchlist.data.store

import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.store5.EitherFetcher
import cinescout.store5.EitherUpdater
import cinescout.store5.MutableStore5
import cinescout.store5.MutableStore5Builder
import cinescout.watchlist.data.datasource.LocalWatchlistDataSource
import cinescout.watchlist.data.datasource.RemoteWatchlistDataSource
import cinescout.watchlist.domain.model.WatchlistStoreKey
import cinescout.watchlist.domain.store.WatchlistIdsStore
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [WatchlistIdsStore::class])
internal class RealWatchlistIdsStore(
    private val localDataSource: LocalWatchlistDataSource,
    private val remoteDataSource: RemoteWatchlistDataSource
) : WatchlistIdsStore,
    MutableStore5<WatchlistStoreKey, List<ScreenplayIds>, Unit> by MutableStore5Builder
        .from<WatchlistStoreKey, List<ScreenplayIds>>(
            fetcher = EitherFetcher.ofOperation { key ->
                require(key is WatchlistStoreKey.Read) { "Write keys are not supported for fetcher" }
                remoteDataSource.getAllWatchlistIds(key.type)
            },
            sourceOfTruth = SourceOfTruth.of(
                reader = { key ->
                    require(key is WatchlistStoreKey.Read) { "Write keys are not supported for reader" }
                    localDataSource.findWatchlistIds(key.type)
                },
                writer = { key, ids ->
                    when (key) {
                        is WatchlistStoreKey.Read -> localDataSource.updateAllWatchlistIds(ids)
                        is WatchlistStoreKey.Write.Add -> localDataSource.insert(key.ids)
                        is WatchlistStoreKey.Write.Remove -> localDataSource.delete(key.id)
                    }
                }
            )
        )
        .build(
            updater = EitherUpdater.byOperation({ key: WatchlistStoreKey, _ ->
                when (key) {
                    is WatchlistStoreKey.Read -> error("Read keys are not supported for updater")
                    is WatchlistStoreKey.Write.Add -> remoteDataSource.postAddToWatchlist(key.ids.tmdb)
                    is WatchlistStoreKey.Write.Remove -> remoteDataSource.postRemoveFromWatchlist(key.id)
                }
            })
        )
