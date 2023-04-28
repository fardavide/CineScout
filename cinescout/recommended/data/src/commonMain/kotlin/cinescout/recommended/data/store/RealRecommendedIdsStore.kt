package cinescout.recommended.data.store

import cinescout.fetchdata.domain.repository.FetchDataRepository
import cinescout.recommended.data.datasource.LocalRecommendedDataSource
import cinescout.recommended.data.datasource.RemoteRecommendedDataSource
import cinescout.recommended.domain.store.RecommendedIdsStore
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth
import kotlin.time.Duration

@Single(binds = [RecommendedIdsStore::class])
internal class RealRecommendedIdsStore(
    private val fetchDataRepository: FetchDataRepository,
    private val localRecommendedDataSource: LocalRecommendedDataSource,
    private val remoteRecommendedDataSource: RemoteRecommendedDataSource
) : RecommendedIdsStore,
    Store5<RecommendedIdsStore.Key, List<ScreenplayIds>> by
    Store5Builder.from<RecommendedIdsStore.Key, List<ScreenplayIds>>(
        fetcher = EitherFetcher.of { key -> remoteRecommendedDataSource.getRecommendedIds(key.type) },
        sourceOfTruth = SourceOfTruth.of(
            reader = { key ->
                localRecommendedDataSource.findRecommendedIds(key.type).map { list ->
                    val wasFetched = fetchDataRepository.get(key, expiration = Duration.INFINITE) != null
                    list.takeIf { wasFetched }
                }
            },
            writer = { key, screenplays ->
                fetchDataRepository.set(key)
                localRecommendedDataSource.insertRecommendedIds(screenplays)
            }
        )
    ).build()
