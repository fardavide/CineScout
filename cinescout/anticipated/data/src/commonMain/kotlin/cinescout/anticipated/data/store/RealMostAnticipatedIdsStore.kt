package cinescout.anticipated.data.store

import cinescout.anticipated.data.datasource.LocalAnticipatedDataSource
import cinescout.anticipated.data.datasource.RemoteAnticipatedDataSource
import cinescout.anticipated.domain.store.MostAnticipatedIdsStore
import cinescout.fetchdata.domain.repository.FetchDataRepository
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth
import kotlin.time.Duration

@Single(binds = [MostAnticipatedIdsStore::class])
internal class RealMostAnticipatedIdsStore(
    private val fetchDataRepository: FetchDataRepository,
    private val localAnticipatedDataSource: LocalAnticipatedDataSource,
    private val remoteAnticipatedDataSource: RemoteAnticipatedDataSource
) : MostAnticipatedIdsStore,
    Store5<MostAnticipatedIdsStore.Key, List<ScreenplayIds>> by
    Store5Builder.from<MostAnticipatedIdsStore.Key, List<ScreenplayIds>>(
        fetcher = EitherFetcher.of { key -> remoteAnticipatedDataSource.getMostAnticipatedIds(key.type) },
        sourceOfTruth = SourceOfTruth.of(
            reader = { key ->
                localAnticipatedDataSource.findMostAnticipatedIds(key.type).map { list ->
                    val wasFetched = fetchDataRepository.get(key, expiration = Duration.INFINITE) != null
                    list.takeIf { wasFetched }
                }
            },
            writer = { key, screenplays ->
                fetchDataRepository.set(key)
                localAnticipatedDataSource.insertMostAnticipatedIds(screenplays)
            }
        )
    ).build()
