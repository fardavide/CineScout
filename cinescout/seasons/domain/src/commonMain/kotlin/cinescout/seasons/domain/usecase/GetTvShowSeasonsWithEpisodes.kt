package cinescout.seasons.domain.usecase

import arrow.core.Either
import cinescout.CineScoutTestApi
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.id.TvShowIds
import cinescout.seasons.domain.model.TvShowSeasonsWithEpisodes
import cinescout.seasons.domain.sample.TvShowSeasonsWithEpisodesSample
import cinescout.seasons.domain.store.TvShowSeasonsWithEpisodesStore
import cinescout.store5.ext.filterData
import cinescout.store5.test.storeFlowOf
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface GetTvShowSeasonsWithEpisodes {

    operator fun invoke(
        tvShowId: TvShowIds,
        refresh: Boolean
    ): Flow<Either<NetworkError, TvShowSeasonsWithEpisodes>>
}

@Factory
internal class GetTvShowSeasonsWithEpisodesImpl(
    private val store: TvShowSeasonsWithEpisodesStore
) : GetTvShowSeasonsWithEpisodes {

    override fun invoke(
        tvShowId: TvShowIds,
        refresh: Boolean
    ): Flow<Either<NetworkError, TvShowSeasonsWithEpisodes>> =
        store.stream(StoreReadRequest.cached(tvShowId, refresh)).filterData()
}

@CineScoutTestApi
class FakeGetTvShowSeasonsWithEpisodes(
    private val tvShowSeasonsWithEpisodes: TvShowSeasonsWithEpisodes = TvShowSeasonsWithEpisodesSample.BreakingBad
) : GetTvShowSeasonsWithEpisodes {

    override fun invoke(
        tvShowId: TvShowIds,
        refresh: Boolean
    ): Flow<Either<NetworkError, TvShowSeasonsWithEpisodes>> =
        storeFlowOf(tvShowSeasonsWithEpisodes).filterData()
}
