package cinescout.trending.domain.store

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.store5.Store5
import cinescout.store5.Store5ReadResponse
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponseOrigin

interface TrendingIdsStore : Store5<TrendingIdsStore.Key, List<ScreenplayIds>> {

    data class Key(val type: ScreenplayTypeFilter)
}

class FakeTrendingIdsStore(
    private val ids: List<ScreenplayIds>? = null,
    private val fetchResult: Either<NetworkError, List<ScreenplayIds>> =
        ids?.right() ?: NetworkError.NotFound.left(),
    private val response: Store5ReadResponse<List<ScreenplayIds>> =
        Store5ReadResponse.Data(fetchResult, StoreReadResponseOrigin.Fetcher)
) : TrendingIdsStore {

    override suspend fun clear() {
        TODO("Not yet implemented")
    }

    override fun stream(request: StoreReadRequest<TrendingIdsStore.Key>): StoreFlow<List<ScreenplayIds>> =
        storeFlowOf(response)
}
