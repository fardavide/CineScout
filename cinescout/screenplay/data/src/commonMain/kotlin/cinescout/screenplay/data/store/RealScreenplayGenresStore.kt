package cinescout.screenplay.data.store

import cinescout.screenplay.data.datasource.LocalScreenplayDataSource
import cinescout.screenplay.data.datasource.RemoteScreenplayDataSource
import cinescout.screenplay.domain.model.ScreenplayGenres
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import cinescout.screenplay.domain.store.ScreenplayGenresStore
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [ScreenplayGenresStore::class])
internal class RealScreenplayGenresStore(
    private val localScreenplayDataSource: LocalScreenplayDataSource,
    private val remoteScreenplayDataSource: RemoteScreenplayDataSource
) : ScreenplayGenresStore,
    Store5<TmdbScreenplayId, ScreenplayGenres> by Store5Builder.from(
        fetcher = EitherFetcher.of(remoteScreenplayDataSource::getScreenplayGenres),
        sourceOfTruth = SourceOfTruth.Companion.of(
            reader = localScreenplayDataSource::findScreenplayGenres,
            writer = { _, value -> localScreenplayDataSource.insertScreenplayGenres(value) }
        )
    ).build()
