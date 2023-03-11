package cinescout.tvshows.data.store

import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import cinescout.tvshows.data.LocalTvShowDataSource
import cinescout.tvshows.data.RemoteTvShowDataSource
import cinescout.tvshows.domain.model.TvShowVideos
import cinescout.tvshows.domain.store.TvShowVideosStore
import cinescout.tvshows.domain.store.TvShowVideosStoreKey
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [TvShowVideosStore::class])
internal class RealTvShowVideosStore(
    private val localTvShowDataSource: LocalTvShowDataSource,
    private val remoteTvShowDataSource: RemoteTvShowDataSource
) : TvShowVideosStore,
    Store5<TvShowVideosStoreKey, TvShowVideos> by Store5Builder
        .from<TvShowVideosStoreKey, TvShowVideos>(
            fetcher = EitherFetcher.of { key -> remoteTvShowDataSource.getTvShowVideos(key.tvShowId) },
            sourceOfTruth = SourceOfTruth.of(
                reader = { key -> localTvShowDataSource.findTvShowVideos(key.tvShowId) },
                writer = { _, value -> localTvShowDataSource.insertVideos(value) }
            )
        )
        .build()
