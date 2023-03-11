package cinescout.tvshows.domain.store

import cinescout.error.NetworkError
import cinescout.store5.Store5
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import cinescout.tvshows.domain.model.TvShow
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface WatchlistTvShowsStore : Store5<Unit, List<TvShow>>

class FakeWatchlistTvShowsStore(
    private val tvShows: List<TvShow>? = null
) : WatchlistTvShowsStore {

    override suspend fun clear() {
        TODO("Not yet implemented")
    }

    override fun stream(request: StoreReadRequest<Unit>): StoreFlow<List<TvShow>> =
        tvShows?.let(::storeFlowOf) ?: storeFlowOf(NetworkError.NotFound)
}
