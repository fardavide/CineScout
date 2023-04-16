package cinescout.screenplay.data.store

import cinescout.screenplay.data.datasource.LocalScreenplayDataSource
import cinescout.screenplay.data.datasource.RemoteScreenplayDataSource
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.store.RecommendedScreenplayIdsStore
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [RecommendedScreenplayIdsStore::class])
internal class RealRecommendedScreenplayIdsStore(
    private val localDataSource: LocalScreenplayDataSource,
    private val remoteDataSource: RemoteScreenplayDataSource
) : RecommendedScreenplayIdsStore,
    Store5<Unit, List<ScreenplayIds>> by Store5Builder.from<Unit, List<ScreenplayIds>>(
        fetcher = EitherFetcher.ofOperation { remoteDataSource.getRecommendedIds() },
        sourceOfTruth = SourceOfTruth.Companion.of(
            reader = { localDataSource.findRecommendedIds() },
            writer = { _, value -> localDataSource.insertRecommendedIds(value) }
        )
    ).build()
