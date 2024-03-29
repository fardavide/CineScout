package cinescout.recommended.domain.store

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.CineScoutTestApi
import cinescout.error.NetworkError
import cinescout.notImplementedFake
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.store5.Store5
import cinescout.store5.Store5ReadResponse
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponseOrigin

interface RecommendedIdsStore : Store5<RecommendedIdsStore.Key, List<ScreenplayIds>> {

    data class Key(val type: ScreenplayTypeFilter)
}

@CineScoutTestApi
class FakeRecommendedIdsStore(
    private val ids: List<ScreenplayIds>? = null,
    private val fetchResult: Either<NetworkError, List<ScreenplayIds>> =
        ids?.right() ?: NetworkError.NotFound.left(),
    private val response: Store5ReadResponse<List<ScreenplayIds>> =
        Store5ReadResponse.Data(fetchResult, StoreReadResponseOrigin.Fetcher())
) : RecommendedIdsStore {

    override suspend fun clear() {
        notImplementedFake()
    }

    override fun stream(request: StoreReadRequest<RecommendedIdsStore.Key>): StoreFlow<List<ScreenplayIds>> =
        storeFlowOf(response)
}
