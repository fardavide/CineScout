package cinescout.history.data.store

import cinescout.history.data.datasource.LocalScreenplayHistoryDataSource
import cinescout.history.data.datasource.RemoteScreenplayHistoryDataSource
import cinescout.history.domain.model.ScreenplayHistory
import cinescout.history.domain.model.ScreenplayHistoryStoreKey
import cinescout.history.domain.store.ScreenplayHistoryStore
import cinescout.store5.EitherFetcher
import cinescout.store5.EitherUpdater
import cinescout.store5.MutableStore5
import cinescout.store5.MutableStore5Builder
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [ScreenplayHistoryStore::class])
internal class RealScreenplayHistoryStore(
    private val localDataSource: LocalScreenplayHistoryDataSource,
    private val remoteDataSource: RemoteScreenplayHistoryDataSource
) : ScreenplayHistoryStore,
    MutableStore5<ScreenplayHistoryStoreKey, ScreenplayHistory, Unit> by MutableStore5Builder
        .from<ScreenplayHistoryStoreKey, ScreenplayHistory>(
            fetcher = EitherFetcher.ofOperation { key ->
                require(key is ScreenplayHistoryStoreKey.Read) { "Only read keys are supported" }
                remoteDataSource.getHistory(key.screenplayIds)
            },
            sourceOfTruth = SourceOfTruth.of(
                reader = { key ->
                    require(key is ScreenplayHistoryStoreKey.Read) { "Only read keys are supported" }
                    localDataSource.find(key.screenplayIds)
                },
                writer = { key, history ->
                    when (key) {
                        is ScreenplayHistoryStoreKey.Read -> localDataSource.insertAll(history)
                        is ScreenplayHistoryStoreKey.Write.Add -> localDataSource.insertPlaceholder(key.screenplayIds)
                        is ScreenplayHistoryStoreKey.Write.Delete -> localDataSource.deleteAll(key.screenplayId)
                    }
                }
            )
        )
        .build(
            updater = EitherUpdater.byOperation({ key, _ ->
                require(key is ScreenplayHistoryStoreKey.Write) { "Only write keys are supported" }
                when (key) {
                    is ScreenplayHistoryStoreKey.Write.Add -> remoteDataSource.addToHistory(key.screenplayIds)
                    is ScreenplayHistoryStoreKey.Write.Delete -> remoteDataSource.deleteHistory(key.screenplayId)
                }
            })
        )
