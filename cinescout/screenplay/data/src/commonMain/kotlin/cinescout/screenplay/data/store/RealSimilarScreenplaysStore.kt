package cinescout.screenplay.data.store

import cinescout.screenplay.data.datasource.LocalScreenplayDataSource
import cinescout.screenplay.data.datasource.RemoteScreenplayDataSource
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.store.SimilarScreenplaysStore
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Factory(binds = [SimilarScreenplaysStore::class])
internal class RealSimilarScreenplaysStore(
    private val localScreenplayDataSource: LocalScreenplayDataSource,
    private val remoteScreenplayDataSource: RemoteScreenplayDataSource
) : SimilarScreenplaysStore,
    Store5<TmdbScreenplayId, List<Screenplay>> by Store5Builder.from<TmdbScreenplayId, List<Screenplay>>(
        fetcher = EitherFetcher.of { key -> remoteScreenplayDataSource.getSimilar(key, page = 1) },
        sourceOfTruth = SourceOfTruth.of(
            reader = { key -> localScreenplayDataSource.findSimilar(key) },
            writer = { key, value -> localScreenplayDataSource.insertSimilar(key, value) }
        )
    ).build()
