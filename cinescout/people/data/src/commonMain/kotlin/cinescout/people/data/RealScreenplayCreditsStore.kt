package cinescout.people.data

import cinescout.people.domain.model.ScreenplayCredits
import cinescout.people.domain.store.ScreenplayCreditsStore
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [ScreenplayCreditsStore::class])
internal class RealScreenplayCreditsStore(
    private val localPeopleDataSource: LocalPeopleDataSource,
    private val remotePeopleDataSource: RemotePeopleDataSource
) : ScreenplayCreditsStore,
    Store5<TmdbScreenplayId, ScreenplayCredits> by Store5Builder.from<TmdbScreenplayId, ScreenplayCredits>(
        fetcher = EitherFetcher.of { screenplayId -> remotePeopleDataSource.getCredits(screenplayId) },
        sourceOfTruth = SourceOfTruth.of(
            reader = { screenplayId -> localPeopleDataSource.findCredits(screenplayId) },
            writer = { _, value -> localPeopleDataSource.insertCredits(value) }
        )
    ).build()
