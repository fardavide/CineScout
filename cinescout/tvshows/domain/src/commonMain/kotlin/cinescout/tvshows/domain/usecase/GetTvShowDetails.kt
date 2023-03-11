package cinescout.tvshows.domain.usecase

import cinescout.store5.StoreFlow
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShowWithDetails
import cinescout.tvshows.domain.store.TvShowDetailsKey
import cinescout.tvshows.domain.store.TvShowDetailsStore
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

@Factory
class GetTvShowDetails(
    private val tvShowDetailsStore: TvShowDetailsStore
) {

    operator fun invoke(tvShowId: TmdbTvShowId, refresh: Boolean = true): StoreFlow<TvShowWithDetails> =
        tvShowDetailsStore.stream(StoreReadRequest.cached(TvShowDetailsKey(tvShowId), refresh = refresh))
}
