package cinescout.people.data.store

import cinescout.fetchdata.domain.repository.FetchDataRepository
import cinescout.people.data.datasource.LocalPeopleDataSource
import cinescout.people.data.datasource.RemotePeopleDataSource
import cinescout.people.domain.model.ScreenplayCredits
import cinescout.people.domain.store.ScreenplayCreditsStore
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth
import kotlin.time.Duration

@Single(binds = [ScreenplayCreditsStore::class])
internal class RealScreenplayCreditsStore(
    private val fetchDataRepository: FetchDataRepository,
    private val localPeopleDataSource: LocalPeopleDataSource,
    private val remotePeopleDataSource: RemotePeopleDataSource
) : ScreenplayCreditsStore,
    Store5<ScreenplayCreditsStore.Key, ScreenplayCredits> by
    Store5Builder.from<ScreenplayCreditsStore.Key, ScreenplayCredits>(
        fetcher = EitherFetcher.of { key -> remotePeopleDataSource.getCredits(key.screenplayId) },
        sourceOfTruth = SourceOfTruth.of(
            reader = { key ->
                localPeopleDataSource.findCredits(key.screenplayId).map { credits ->
                    val wasFetched = fetchDataRepository.get(key, expiration = Duration.INFINITE) != null
                    credits.takeIf { wasFetched }
                }
            },
            writer = { key, value ->
                fetchDataRepository.set(key)
                localPeopleDataSource.insertCredits(value)
            }
        )
    ).build()
