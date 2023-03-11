package cinescout.tvshows.data.store

import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import cinescout.tvshows.data.LocalTvShowDataSource
import cinescout.tvshows.data.RemoteTvShowDataSource
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.ids
import cinescout.tvshows.domain.store.WatchlistTvShowIdsStore
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [WatchlistTvShowIdsStore::class])
internal class RealWatchlistTvShowIdsStore(
    private val localTvShowDataSource: LocalTvShowDataSource,
    private val remoteTvShowDataSource: RemoteTvShowDataSource
) : WatchlistTvShowIdsStore,
    Store5<Unit, List<TmdbTvShowId>> by Store5Builder
        .from<Unit, List<TmdbTvShowId>>(
            fetcher = EitherFetcher.ofOperation { remoteTvShowDataSource.getWatchlistTvShows() },
            sourceOfTruth = SourceOfTruth.of(
                reader = { localTvShowDataSource.findAllWatchlistTvShows().ids() },
                writer = { _, value -> localTvShowDataSource.insertWatchlistIds(value) }
            )
        )
        .build()
