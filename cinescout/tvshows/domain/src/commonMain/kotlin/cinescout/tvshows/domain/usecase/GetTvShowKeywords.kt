package cinescout.tvshows.domain.usecase

import cinescout.store5.StoreFlow
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShowKeywords
import cinescout.tvshows.domain.store.TvShowKeywordsKey
import cinescout.tvshows.domain.store.TvShowKeywordsStore
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

@Factory
class GetTvShowKeywords(
    private val tvShowKeywordsStore: TvShowKeywordsStore
) {

    operator fun invoke(tvShowId: TmdbTvShowId, refresh: Boolean = true): StoreFlow<TvShowKeywords> =
        tvShowKeywordsStore.stream(StoreReadRequest.cached(TvShowKeywordsKey(tvShowId), refresh = refresh))
}
