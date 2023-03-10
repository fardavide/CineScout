package cinescout.tvshows.data.store

import cinescout.error.NetworkError
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import cinescout.tvshows.data.LocalTvShowDataSource
import cinescout.tvshows.data.RemoteTvShowDataSource
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.model.ids
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface WatchlistTvShowIdsStore : Store5<Unit, List<TmdbTvShowId>>

@Single(binds = [WatchlistTvShowIdsStore::class])
class RealWatchlistTvShowIdsStore(
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

class FakeWatchlistTvShowIdsStore(
    private val tvShows: List<TvShow>? = null,
    private val tvShowIds: List<TmdbTvShowId>? =
        tvShows?.map { it.tmdbId }
) : WatchlistTvShowIdsStore {

    override fun stream(request: StoreReadRequest<Unit>): StoreFlow<List<TmdbTvShowId>> =
        tvShowIds?.let(::storeFlowOf) ?: storeFlowOf(NetworkError.NotFound)
}
