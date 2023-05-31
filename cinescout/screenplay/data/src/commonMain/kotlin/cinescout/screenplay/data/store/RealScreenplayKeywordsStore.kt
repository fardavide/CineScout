package cinescout.screenplay.data.store

import cinescout.screenplay.data.datasource.LocalScreenplayDataSource
import cinescout.screenplay.data.datasource.RemoteScreenplayDataSource
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import cinescout.screenplay.domain.store.ScreenplayKeywordsStore
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [ScreenplayKeywordsStore::class])
internal class RealScreenplayKeywordsStore(
    private val localScreenplayDataSource: LocalScreenplayDataSource,
    private val remoteScreenplayDataSource: RemoteScreenplayDataSource
) : ScreenplayKeywordsStore,
    Store5<TmdbScreenplayId, ScreenplayKeywords> by Store5Builder.from(
        fetcher = EitherFetcher.of(remoteScreenplayDataSource::getScreenplayKeywords),
        sourceOfTruth = SourceOfTruth.Companion.of(
            reader = localScreenplayDataSource::findScreenplayKeywords,
            writer = { _, value -> localScreenplayDataSource.insertScreenplayKeywords(value) }
        )
    ).build()
