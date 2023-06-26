package cinescout.screenplay.data.store

import arrow.core.raise.either
import cinescout.screenplay.data.datasource.LocalScreenplayDataSource
import cinescout.screenplay.data.datasource.RemoteScreenplayDataSource
import cinescout.screenplay.domain.model.ScreenplayGenres
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.screenplay.domain.store.ScreenplayGenresStore
import cinescout.screenplay.domain.store.ScreenplayStore
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [ScreenplayGenresStore::class])
internal class RealScreenplayGenresStore(
    private val localScreenplayDataSource: LocalScreenplayDataSource,
    private val remoteScreenplayDataSource: RemoteScreenplayDataSource,
    private val screenplayStore: ScreenplayStore
) : ScreenplayGenresStore,
    Store5<ScreenplayIds, ScreenplayGenres> by Store5Builder.from<ScreenplayIds, ScreenplayGenres>(
        fetcher = EitherFetcher.of { screenplayIds ->
            either {
                val screenplayWithGenreSlugs = screenplayStore.getCached(screenplayIds, refresh = false).bind()
                val allGenres = remoteScreenplayDataSource.getAllGenres().bind()
                ScreenplayGenres(
                    genres = allGenres.filter { it.slug in screenplayWithGenreSlugs.genreSlugs },
                    screenplayIds = screenplayWithGenreSlugs.screenplay.ids
                )
            }
        },
        sourceOfTruth = SourceOfTruth.Companion.of(
            reader = { screenplayIds -> localScreenplayDataSource.findScreenplayGenres(screenplayIds) },
            writer = { _, screenplayGenres -> localScreenplayDataSource.insertScreenplayGenres(screenplayGenres) }
        )
    ).build()
