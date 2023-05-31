package cinescout.popular.data.store

import cinescout.fetchdata.domain.repository.FetchDataRepository
import cinescout.popular.data.datasource.LocalPopularDataSource
import cinescout.popular.data.datasource.RemotePopularDataSource
import cinescout.popular.domain.store.PopularIdsStore
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth
import kotlin.time.Duration

@Single(binds = [PopularIdsStore::class])
internal class RealPopularIdsStore(
    private val fetchDataRepository: FetchDataRepository,
    private val localPopularDataSource: LocalPopularDataSource,
    private val remotePopularDataSource: RemotePopularDataSource
) : PopularIdsStore,
    Store5<PopularIdsStore.Key, List<ScreenplayIds>> by
    Store5Builder.from<PopularIdsStore.Key, List<ScreenplayIds>>(
        fetcher = EitherFetcher.of { key -> remotePopularDataSource.getPopularIds(key.type) },
        sourceOfTruth = SourceOfTruth.of(
            reader = { key ->
                localPopularDataSource.findPopularIds(key.type).map { list ->
                    val wasFetched = fetchDataRepository.get(key, expiration = Duration.INFINITE) != null
                    list.takeIf { wasFetched }
                }
            },
            writer = { key, screenplays ->
                fetchDataRepository.set(key)
                localPopularDataSource.insertPopularIds(screenplays)
            }
        )
    ).build()
