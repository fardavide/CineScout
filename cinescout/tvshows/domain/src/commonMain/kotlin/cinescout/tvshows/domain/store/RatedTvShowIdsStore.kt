package cinescout.tvshows.domain.store

import cinescout.error.NetworkError
import cinescout.store5.Store5
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import cinescout.tvshows.domain.model.TvShowIdWithPersonalRating
import cinescout.tvshows.domain.model.TvShowWithPersonalRating
import cinescout.tvshows.domain.model.ids
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface RatedTvShowIdsStore : Store5<Unit, List<TvShowIdWithPersonalRating>>

class FakeRatedTvShowIdsStore(
    private val tvShows: List<TvShowWithPersonalRating>? = null,
    private val tvShowIds: List<TvShowIdWithPersonalRating>? = tvShows?.ids()
) : RatedTvShowIdsStore {

    override suspend fun clear() {
        TODO("Not yet implemented")
    }

    override fun stream(request: StoreReadRequest<Unit>): StoreFlow<List<TvShowIdWithPersonalRating>> =
        tvShowIds?.let(::storeFlowOf) ?: storeFlowOf(NetworkError.NotFound)
}
