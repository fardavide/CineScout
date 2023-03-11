package cinescout.tvshows.domain.store

import cinescout.store5.Store5
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShowCredits
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface TvShowCreditsStore : Store5<TvShowCreditsKey, TvShowCredits>

@JvmInline
value class TvShowCreditsKey(val tvShowId: TmdbTvShowId)

class FakeTvShowCreditsStore(private val tvShowCredits: List<TvShowCredits>) :
    TvShowCreditsStore {

    override suspend fun clear() {
        TODO("Not yet implemented")
    }

    override fun stream(request: StoreReadRequest<TvShowCreditsKey>): StoreFlow<TvShowCredits> =
        storeFlowOf(tvShowCredits.first { it.tvShowId == request.key.tvShowId })
}
