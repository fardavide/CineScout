package cinescout.tvshows.data.store

import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import cinescout.tvshows.data.LocalTvShowDataSource
import cinescout.tvshows.data.RemoteTvShowDataSource
import cinescout.tvshows.domain.model.TvShowKeywords
import cinescout.tvshows.domain.store.TvShowKeywordsKey
import cinescout.tvshows.domain.store.TvShowKeywordsStore
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [TvShowKeywordsStore::class])
internal class RealTvShowKeywordsStore(
    private val localTvShowDataSource: LocalTvShowDataSource,
    private val remoteTvShowDataSource: RemoteTvShowDataSource
) : TvShowKeywordsStore,
    Store5<TvShowKeywordsKey, TvShowKeywords> by Store5Builder
        .from<TvShowKeywordsKey, TvShowKeywords>(
            fetcher = EitherFetcher.of { key -> remoteTvShowDataSource.getTvShowKeywords(key.tvShowId) },
            sourceOfTruth = SourceOfTruth.Companion.of(
                reader = { key -> localTvShowDataSource.findTvShowKeywords(key.tvShowId) },
                writer = { _, value -> localTvShowDataSource.insertKeywords(value) }
            )
        )
        .build()
