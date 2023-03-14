package cinescout.media.data.store

import cinescout.media.data.datasource.LocalMediaDataSource
import cinescout.media.data.datasource.RemoteMediaDataSource
import cinescout.media.domain.model.ScreenplayVideos
import cinescout.media.domain.store.ScreenplayVideosStore
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [ScreenplayVideosStore::class])
class RealScreenplayVideosStore(
    private val localMediaDataSource: LocalMediaDataSource,
    private val remoteMediaDataSource: RemoteMediaDataSource
) : ScreenplayVideosStore,
    Store5<TmdbScreenplayId, ScreenplayVideos> by Store5Builder
        .from<TmdbScreenplayId, ScreenplayVideos>(
            fetcher = EitherFetcher.of { key -> remoteMediaDataSource.getVideos(key) },
            sourceOfTruth = SourceOfTruth.of(
                reader = { key -> localMediaDataSource.findVideos(key) },
                writer = { _, value -> localMediaDataSource.insertVideos(value) }
            )
        )
        .build()
