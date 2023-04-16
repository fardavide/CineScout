package cinescout.screenplay.data.store

import cinescout.screenplay.data.datasource.LocalScreenplayDataSource
import cinescout.screenplay.data.datasource.RemoteScreenplayDataSource
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.ScreenplayMemoryPolicy
import cinescout.screenplay.domain.store.ScreenplayStore
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [ScreenplayStore::class])
internal class RealScreenplayStore(
    private val localScreenplayDataSource: LocalScreenplayDataSource,
    private val remoteScreenplayDataSource: RemoteScreenplayDataSource
) : ScreenplayStore,
    Store5<ScreenplayIds, Screenplay> by Store5Builder.from(
        fetcher = EitherFetcher.of(remoteScreenplayDataSource::getScreenplay),
        sourceOfTruth = SourceOfTruth.of(
            reader = localScreenplayDataSource::findScreenplay,
            writer = { _, value -> localScreenplayDataSource.insert(value) }
        )
    )
        .cachePolicy(ScreenplayMemoryPolicy())
        .build()
