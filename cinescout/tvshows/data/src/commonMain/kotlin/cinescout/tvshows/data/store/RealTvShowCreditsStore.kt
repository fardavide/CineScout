package cinescout.tvshows.data.store

import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import cinescout.tvshows.data.LocalTvShowDataSource
import cinescout.tvshows.data.RemoteTvShowDataSource
import cinescout.tvshows.domain.model.TvShowCredits
import cinescout.tvshows.domain.store.TvShowCreditsKey
import cinescout.tvshows.domain.store.TvShowCreditsStore
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [TvShowCreditsStore::class])
internal class RealTvShowCreditsStore(
    private val localTvShowDataSource: LocalTvShowDataSource,
    private val remoteTvShowDataSource: RemoteTvShowDataSource
) : TvShowCreditsStore,
    Store5<TvShowCreditsKey, TvShowCredits> by Store5Builder
        .from<TvShowCreditsKey, TvShowCredits>(
            fetcher = EitherFetcher.of { key -> remoteTvShowDataSource.getTvShowCredits(key.tvShowId) },
            sourceOfTruth = SourceOfTruth.Companion.of(
                reader = { key -> localTvShowDataSource.findTvShowCredits(key.tvShowId) },
                writer = { _, value -> localTvShowDataSource.insertCredits(value) }
            )
        )
        .build()
