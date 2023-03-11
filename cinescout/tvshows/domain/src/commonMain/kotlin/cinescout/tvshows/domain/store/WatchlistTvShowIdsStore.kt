package cinescout.tvshows.domain.store

import cinescout.error.NetworkError
import cinescout.store5.Store5
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShow
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface WatchlistTvShowIdsStore : Store5<Unit, List<TmdbTvShowId>>

class FakeWatchlistTvShowIdsStore(
    private val tvShows: List<TvShow>? = null,
    private val tvShowIds: List<TmdbTvShowId>? =
        tvShows?.map { it.tmdbId }
) : WatchlistTvShowIdsStore {

    override fun stream(request: StoreReadRequest<Unit>): StoreFlow<List<TmdbTvShowId>> =
        tvShowIds?.let(::storeFlowOf) ?: storeFlowOf(NetworkError.NotFound)
}
