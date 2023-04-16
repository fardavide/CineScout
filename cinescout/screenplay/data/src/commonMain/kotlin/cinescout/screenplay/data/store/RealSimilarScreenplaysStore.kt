package cinescout.screenplay.data.store

import cinescout.screenplay.data.datasource.LocalScreenplayDataSource
import cinescout.screenplay.data.datasource.RemoteScreenplayDataSource
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.store.SimilarScreenplaysStore
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [SimilarScreenplaysStore::class])
internal class RealSimilarScreenplaysStore(
    private val localScreenplayDataSource: LocalScreenplayDataSource,
    private val remoteScreenplayDataSource: RemoteScreenplayDataSource
) : SimilarScreenplaysStore,
    Store5<ScreenplayIds, List<Screenplay>> by Store5Builder.from(
        fetcher = EitherFetcher.of { key -> remoteScreenplayDataSource.getSimilar(key, page = 1) },
        sourceOfTruth = SourceOfTruth.of(
            reader = localScreenplayDataSource::findSimilar,
            writer = localScreenplayDataSource::insertSimilar
        )
    ).build()
