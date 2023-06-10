package cinescout.seasons.domain.store

import cinescout.CineScoutTestApi
import cinescout.screenplay.domain.model.ids.TvShowIds
import cinescout.seasons.domain.model.TvShowSeasonsWithEpisodes
import cinescout.seasons.domain.sample.TvShowSeasonsWithEpisodesSample
import cinescout.store5.Store5
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface TvShowSeasonsWithEpisodesStore : Store5<TvShowIds, TvShowSeasonsWithEpisodes>

@CineScoutTestApi
class FakeTvShowSeasonsWithEpisodesStore(
    private val tvShowSeasonsWithEpisodes: TvShowSeasonsWithEpisodes = TvShowSeasonsWithEpisodesSample.BreakingBad
) : TvShowSeasonsWithEpisodesStore {

    override suspend fun clear() = Unit

    override fun stream(request: StoreReadRequest<TvShowIds>): StoreFlow<TvShowSeasonsWithEpisodes> =
        storeFlowOf(tvShowSeasonsWithEpisodes)
}
