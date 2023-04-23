package cinescout.anticipated.data.store

import cinescout.anticipated.data.datasource.LocalAnticipatedDataSource
import cinescout.anticipated.data.datasource.RemoteAnticipatedDataSource
import cinescout.anticipated.domain.store.MostAnticipatedStore
import cinescout.fetchdata.domain.repository.FetchDataRepository
import cinescout.screenplay.domain.model.Screenplay
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth
import kotlin.time.Duration

@Single(binds = [MostAnticipatedStore::class])
internal class RealMostAnticipatedStore(
    private val fetchDataRepository: FetchDataRepository,
    private val localAnticipatedDataSource: LocalAnticipatedDataSource,
    private val remoteAnticipatedDataSource: RemoteAnticipatedDataSource
) : MostAnticipatedStore,
    Store5<MostAnticipatedStore.Key, List<Screenplay>> by
    Store5Builder.from<MostAnticipatedStore.Key, List<Screenplay>>(
        fetcher = EitherFetcher.of { key -> remoteAnticipatedDataSource.getMostAnticipated(key.type) },
        sourceOfTruth = SourceOfTruth.of(
            reader = { key ->
                localAnticipatedDataSource.findMostAnticipated(key.type).map { list ->
                    val wasFetched = fetchDataRepository.get(key, expiration = Duration.INFINITE) != null
                    list.takeIf { wasFetched }
                }
            },
            writer = { key, screenplays ->
                fetchDataRepository.set(key)
                localAnticipatedDataSource.insertMostAnticipated(screenplays)
            }
        )
    ).build()
