package cinescout.screenplay.data.store

import cinescout.screenplay.data.datasource.LocalScreenplayDataSource
import cinescout.screenplay.data.datasource.RemoteScreenplayDataSource
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.screenplay.domain.store.PersonalRecommendedScreenplayIdsStore
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [PersonalRecommendedScreenplayIdsStore::class])
internal class RealPersonalRecommendedScreenplayIdsStore(
    private val localDataSource: LocalScreenplayDataSource,
    private val remoteDataSource: RemoteScreenplayDataSource
) : PersonalRecommendedScreenplayIdsStore,
    Store5<Unit, List<ScreenplayIds>> by Store5Builder.from<Unit, List<ScreenplayIds>>(
        fetcher = EitherFetcher.ofOperation { remoteDataSource.getRecommendedIds() },
        sourceOfTruth = SourceOfTruth.Companion.of(
            reader = { localDataSource.findRecommendedIds() },
            writer = { _, value -> localDataSource.insertRecommendedIds(value) }
        )
    ).build()
