package cinescout.tvshows.data.store

import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import cinescout.tvshows.data.LocalTvShowDataSource
import cinescout.tvshows.data.RemoteTvShowDataSource
import cinescout.tvshows.domain.model.TvShowImages
import cinescout.tvshows.domain.store.TvShowImagesStore
import cinescout.tvshows.domain.store.TvShowImagesStoreKey
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [TvShowImagesStore::class])
internal class RealTvShowImagesStore(
    private val localTvShowDataSource: LocalTvShowDataSource,
    private val remoteTvShowDataSource: RemoteTvShowDataSource
) : TvShowImagesStore,
    Store5<TvShowImagesStoreKey, TvShowImages> by Store5Builder
        .from<TvShowImagesStoreKey, TvShowImages>(
            fetcher = EitherFetcher.of { key -> remoteTvShowDataSource.getTvShowImages(key.tvShowId) },
            sourceOfTruth = SourceOfTruth.of(
                reader = { key -> localTvShowDataSource.findTvShowImages(key.tvShowId) },
                writer = { _, value -> localTvShowDataSource.insertImages(value) }
            )
        )
        .build()
