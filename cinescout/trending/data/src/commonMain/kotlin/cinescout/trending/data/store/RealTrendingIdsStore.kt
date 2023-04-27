package cinescout.trending.data.store

import cinescout.fetchdata.domain.repository.FetchDataRepository
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import cinescout.trending.data.datasource.LocalTrendingDataSource
import cinescout.trending.data.datasource.RemoteTrendingDataSource
import cinescout.trending.domain.store.TrendingIdsStore
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth
import kotlin.time.Duration

@Single(binds = [TrendingIdsStore::class])
internal class RealTrendingIdsStore(
    private val fetchDataRepository: FetchDataRepository,
    private val localTrendingDataSource: LocalTrendingDataSource,
    private val remoteTrendingDataSource: RemoteTrendingDataSource
) : TrendingIdsStore,
    Store5<TrendingIdsStore.Key, List<ScreenplayIds>> by
    Store5Builder.from<TrendingIdsStore.Key, List<ScreenplayIds>>(
        fetcher = EitherFetcher.of { key -> remoteTrendingDataSource.getTrendingIds(key.type) },
        sourceOfTruth = SourceOfTruth.of(
            reader = { key ->
                localTrendingDataSource.findTrendingIds(key.type).map { list ->
                    val wasFetched = fetchDataRepository.get(key, expiration = Duration.INFINITE) != null
                    list.takeIf { wasFetched }
                }
            },
            writer = { key, screenplays ->
                fetchDataRepository.set(key)
                localTrendingDataSource.insertTrendingIds(screenplays)
            }
        )
    ).build()
