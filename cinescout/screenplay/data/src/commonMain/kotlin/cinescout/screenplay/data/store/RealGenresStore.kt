package cinescout.screenplay.data.store

import arrow.core.Nel
import cinescout.screenplay.data.datasource.LocalScreenplayDataSource
import cinescout.screenplay.data.datasource.RemoteScreenplayDataSource
import cinescout.screenplay.domain.model.Genre
import cinescout.screenplay.domain.store.GenresStore
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [GenresStore::class])
internal class RealGenresStore(
    private val localScreenplayDataSource: LocalScreenplayDataSource,
    private val remoteScreenplayDataSource: RemoteScreenplayDataSource
) : GenresStore,
    Store5<Unit, Nel<Genre>> by Store5Builder.from<Unit, Nel<Genre>>(
        fetcher = EitherFetcher.of { remoteScreenplayDataSource.getAllGenres() },
        sourceOfTruth = SourceOfTruth.Companion.of(
            reader = { localScreenplayDataSource.findAllGenres() },
            writer = { _, genres -> localScreenplayDataSource.insertGenres(genres) }
        )
    ).build()
