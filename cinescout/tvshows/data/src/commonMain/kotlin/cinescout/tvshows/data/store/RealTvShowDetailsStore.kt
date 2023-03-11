package cinescout.tvshows.data.store

import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import cinescout.tvshows.data.LocalTvShowDataSource
import cinescout.tvshows.data.RemoteTvShowDataSource
import cinescout.tvshows.domain.model.TvShowWithDetails
import cinescout.tvshows.domain.store.TvShowDetailsKey
import cinescout.tvshows.domain.store.TvShowDetailsStore
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [TvShowDetailsStore::class])
internal class RealTvShowDetailsStore(
    private val localTvShowDataSource: LocalTvShowDataSource,
    private val remoteTvShowDataSource: RemoteTvShowDataSource
) : TvShowDetailsStore,
    Store5<TvShowDetailsKey, TvShowWithDetails> by Store5Builder
        .from<TvShowDetailsKey, TvShowWithDetails>(
            fetcher = EitherFetcher.of { key -> remoteTvShowDataSource.getTvShowDetails(key.tvShowId) },
            sourceOfTruth = SourceOfTruth.Companion.of(
                reader = { key -> localTvShowDataSource.findTvShowWithDetails(key.tvShowId) },
                writer = { _, value -> localTvShowDataSource.insert(value) }
            )
        )
        .build()
