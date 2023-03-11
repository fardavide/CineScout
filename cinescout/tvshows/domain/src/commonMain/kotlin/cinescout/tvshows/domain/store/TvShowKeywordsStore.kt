package cinescout.tvshows.domain.store

import cinescout.store5.Store5
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShowKeywords
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface TvShowKeywordsStore : Store5<TvShowKeywordsKey, TvShowKeywords>

@JvmInline
value class TvShowKeywordsKey(val tvShowId: TmdbTvShowId)

class FakeTvShowKeywordsStore(private val tvShowsKeywords: List<TvShowKeywords>) :
    TvShowKeywordsStore {

    override suspend fun clear() {
        TODO("Not yet implemented")
    }

    override fun stream(request: StoreReadRequest<TvShowKeywordsKey>): StoreFlow<TvShowKeywords> =
        storeFlowOf(tvShowsKeywords.first { it.tvShowId == request.key.tvShowId })
}
