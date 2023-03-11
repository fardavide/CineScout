package cinescout.tvshows.domain.store

import cinescout.store5.Store5
import cinescout.store5.StoreFlow
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShowImages
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface TvShowImagesStore : Store5<TvShowImagesStoreKey, TvShowImages>

@JvmInline
value class TvShowImagesStoreKey(val tvShowId: TmdbTvShowId)

class FakeTvShowImagesStore : TvShowImagesStore {

    override fun stream(request: StoreReadRequest<TvShowImagesStoreKey>): StoreFlow<TvShowImages> {
        TODO("Not yet implemented")
    }
}
