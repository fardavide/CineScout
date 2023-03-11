package cinescout.tvshows.domain.store

import cinescout.error.NetworkError
import cinescout.store5.Store5
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import cinescout.tvshows.domain.model.TvShowWithPersonalRating
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface RatedTvShowsStore : Store5<Unit, List<TvShowWithPersonalRating>>

class FakeRatedTvShowsStore(
    private val tvShows: List<TvShowWithPersonalRating>? = null
) : RatedTvShowsStore {

    override suspend fun clear() {
        TODO("Not yet implemented")
    }

    override fun stream(request: StoreReadRequest<Unit>): StoreFlow<List<TvShowWithPersonalRating>> =
        tvShows?.let(::storeFlowOf) ?: storeFlowOf(NetworkError.NotFound)
}
