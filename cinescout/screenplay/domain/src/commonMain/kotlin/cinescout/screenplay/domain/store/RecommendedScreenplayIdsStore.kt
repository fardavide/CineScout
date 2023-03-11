package cinescout.screenplay.domain.store

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.store5.Store5
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface RecommendedScreenplayIdsStore : Store5<Unit, List<TmdbScreenplayId>>

class FakeRecommendedScreenplayIdsStore(
    private val recommendedIds: List<TmdbScreenplayId>? = null,
    private val recommendedIdsResult: Either<NetworkError, List<TmdbScreenplayId>> =
        recommendedIds?.right() ?: NetworkError.NotFound.left()
) : RecommendedScreenplayIdsStore {

    override suspend fun clear() {
        TODO("Not yet implemented")
    }

    override fun stream(request: StoreReadRequest<Unit>): StoreFlow<List<TmdbScreenplayId>> =
        storeFlowOf(recommendedIdsResult)
}
