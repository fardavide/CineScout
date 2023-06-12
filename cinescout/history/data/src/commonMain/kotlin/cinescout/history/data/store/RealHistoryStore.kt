package cinescout.history.data.store

import cinescout.history.data.datasource.LocalScreenplayHistoryDataSource
import cinescout.history.data.datasource.RemoteScreenplayHistoryDataSource
import cinescout.history.domain.model.HistoryStoreKey
import cinescout.history.domain.model.ScreenplayHistory
import cinescout.history.domain.store.HistoryStore
import cinescout.store5.EitherFetcher
import cinescout.store5.EitherUpdater
import cinescout.store5.MutableStore5
import cinescout.store5.MutableStore5Builder
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [HistoryStore::class])
internal class RealHistoryStore(
    private val localDataSource: LocalScreenplayHistoryDataSource,
    private val remoteDataSource: RemoteScreenplayHistoryDataSource
) : HistoryStore,
    MutableStore5<HistoryStoreKey, ScreenplayHistory, Unit> by MutableStore5Builder
        .from<HistoryStoreKey, ScreenplayHistory>(
            fetcher = EitherFetcher.ofOperation { key ->
                require(key is HistoryStoreKey.Read) { "Only read keys are supported" }
                remoteDataSource.getHistory(key.screenplayIds)
            },
            sourceOfTruth = SourceOfTruth.of(
                reader = { key ->
                    require(key is HistoryStoreKey.Read) { "Only read keys are supported" }
                    localDataSource.find(key.screenplayIds)
                },
                writer = { key, history ->
                    when (key) {
                        is HistoryStoreKey.Read -> localDataSource.insertAll(history)
                        is HistoryStoreKey.Write.Add -> localDataSource.insertPlaceholder(key.screenplayIds)
                        is HistoryStoreKey.Write.Delete -> localDataSource.deleteAll(key.screenplayId)
                    }
                }
            )
        )
        .build(
            updater = EitherUpdater.byOperation({ key, _ ->
                require(key is HistoryStoreKey.Write) { "Only write keys are supported" }
                when (key) {
                    is HistoryStoreKey.Write.Add -> remoteDataSource.addToHistory(key.screenplayIds)
                    is HistoryStoreKey.Write.Delete -> remoteDataSource.deleteHistory(key.screenplayId)
                }
            })
        )
