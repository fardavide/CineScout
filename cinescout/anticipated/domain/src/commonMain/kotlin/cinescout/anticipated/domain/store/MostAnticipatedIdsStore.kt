package cinescout.anticipated.domain.store

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.store5.Store5
import cinescout.store5.Store5ReadResponse
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponseOrigin

interface MostAnticipatedIdsStore : Store5<MostAnticipatedIdsStore.Key, List<ScreenplayIds>> {

    data class Key(val type: ScreenplayType)
}

class FakeMostAnticipatedIdsStore(
    private val ids: List<ScreenplayIds>? = null,
    private val fetchResult: Either<NetworkError, List<ScreenplayIds>> =
        ids?.right() ?: NetworkError.NotFound.left(),
    private val response: Store5ReadResponse<List<ScreenplayIds>> =
        Store5ReadResponse.Data(fetchResult, StoreReadResponseOrigin.Fetcher)
) : MostAnticipatedIdsStore {

    override suspend fun clear() {
        TODO("Not yet implemented")
    }

    override fun stream(
        request: StoreReadRequest<MostAnticipatedIdsStore.Key>
    ): StoreFlow<List<ScreenplayIds>> = storeFlowOf(response)
}
