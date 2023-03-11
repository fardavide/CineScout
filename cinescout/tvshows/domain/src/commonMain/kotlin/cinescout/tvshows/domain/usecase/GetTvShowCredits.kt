package cinescout.tvshows.domain.usecase

import cinescout.store5.StoreFlow
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShowCredits
import cinescout.tvshows.domain.store.TvShowCreditsKey
import cinescout.tvshows.domain.store.TvShowCreditsStore
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

@Factory
class GetTvShowCredits(
    private val tvShowCreditsStore: TvShowCreditsStore
) {

    operator fun invoke(id: TmdbTvShowId, refresh: Boolean = true): StoreFlow<TvShowCredits> =
        tvShowCreditsStore.stream(StoreReadRequest.cached(TvShowCreditsKey(id), refresh = refresh))
}
