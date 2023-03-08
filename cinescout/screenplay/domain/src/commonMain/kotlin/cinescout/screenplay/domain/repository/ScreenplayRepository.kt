package cinescout.screenplay.domain.repository

import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.TmdbScreenplayId
import store.Refresh
import store.Store

interface ScreenplayRepository {

    /**
     * Movies and Tv Shows recommended by the service (Trakt).
     */
    fun getRecommended(refresh: Refresh = Refresh.IfExpired()): Store<List<Screenplay>>

    /**
     * [TmdbScreenplayId] of Movies and Tv Shows recommended by the service (Trakt).
     */
    fun getRecommendedIds(refresh: Refresh = Refresh.IfExpired()): Store<List<TmdbScreenplayId>>
}

class FakeScreenplayRepository : ScreenplayRepository {

    override fun getRecommended(refresh: Refresh): Store<List<Screenplay>> {
        TODO("Not yet implemented")
    }

    override fun getRecommendedIds(refresh: Refresh): Store<List<TmdbScreenplayId>> {
        TODO("Not yet implemented")
    }
}
