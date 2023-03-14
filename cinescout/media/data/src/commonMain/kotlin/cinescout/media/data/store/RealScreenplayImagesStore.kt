package cinescout.media.data.store

import cinescout.media.data.datasource.LocalMediaDataSource
import cinescout.media.data.datasource.RemoteMediaDataSource
import cinescout.media.domain.model.ScreenplayImages
import cinescout.media.domain.store.ScreenplayImagesStore
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [ScreenplayImagesStore::class])
internal class RealScreenplayImagesStore(
    private val localMediaDataSource: LocalMediaDataSource,
    private val remoteMediaDataSource: RemoteMediaDataSource
) : ScreenplayImagesStore,
    Store5<TmdbScreenplayId, ScreenplayImages> by Store5Builder.from<TmdbScreenplayId, ScreenplayImages>(
        fetcher = EitherFetcher.of { screenplayId -> remoteMediaDataSource.getImages(screenplayId) },
        sourceOfTruth = SourceOfTruth.of(
            reader = { screenplayId -> localMediaDataSource.findImages(screenplayId) },
            writer = { _, value -> localMediaDataSource.insertImages(value) }
        )
    ).build()
