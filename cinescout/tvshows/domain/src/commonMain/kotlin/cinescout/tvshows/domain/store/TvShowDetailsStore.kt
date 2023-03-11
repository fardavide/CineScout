package cinescout.tvshows.domain.store

import cinescout.store5.Store5
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShowWithDetails
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface TvShowDetailsStore : Store5<TvShowDetailsKey, TvShowWithDetails>

@JvmInline
value class TvShowDetailsKey(val tvShowId: TmdbTvShowId)

class FakeTvShowDetailsStore(private val tvShowsDetails: List<TvShowWithDetails>) :
    TvShowDetailsStore {

    override fun stream(request: StoreReadRequest<TvShowDetailsKey>): StoreFlow<TvShowWithDetails> =
        storeFlowOf(tvShowsDetails.first { it.tvShow.tmdbId == request.key.tvShowId })
}
