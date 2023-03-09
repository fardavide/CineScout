package cinescout.screenplay.domain.repository

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.TmdbScreenplayId
import store.Refresh
import store.Store
import store.builder.storeOf

interface ScreenplayRepository {

    /**
     * [TmdbScreenplayId] of Movies and Tv Shows recommended by the service (Trakt).
     */
    fun getRecommendedIds(refresh: Refresh = Refresh.IfExpired()): Store<List<TmdbScreenplayId>>
}

class FakeScreenplayRepository(
    private val recommendedIds: List<TmdbScreenplayId>? = null,
    private val recommendedIdsResult: Either<DataError, List<TmdbScreenplayId>> =
        recommendedIds?.right() ?: DataError.Remote(NetworkError.NotFound).left()
) : ScreenplayRepository {

    override fun getRecommendedIds(refresh: Refresh): Store<List<TmdbScreenplayId>> =
        storeOf(recommendedIdsResult)
}
