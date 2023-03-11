package cinescout.screenplay.data.store

import cinescout.screenplay.data.datasource.LocalScreenplayDataSource
import cinescout.screenplay.data.datasource.RemoteScreenplayDataSource
import cinescout.screenplay.domain.model.TmdbScreenplayId
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
    Store5<Unit, List<TmdbScreenplayId>> by Store5Builder.from<Unit, List<TmdbScreenplayId>>(
        fetcher = EitherFetcher.ofOperation { remoteDataSource.getRecommended() },
        sourceOfTruth = SourceOfTruth.Companion.of(
            reader = { localDataSource.findRecommendedIds() },
            writer = { _, value -> localDataSource.insertRecommendedIds(value) }
        )
    ).build()
