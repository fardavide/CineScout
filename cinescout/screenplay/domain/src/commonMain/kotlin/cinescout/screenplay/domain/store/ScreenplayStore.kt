package cinescout.screenplay.domain.store

import cinescout.CineScoutTestApi
import cinescout.error.NetworkError
import cinescout.notImplementedFake
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.store5.Store5
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface ScreenplayStore : Store5<ScreenplayIds, Screenplay>

@CineScoutTestApi
class FakeScreenplayStore(
    private val screenplay: Screenplay? = null
) : ScreenplayStore {

    override suspend fun clear() {
        notImplementedFake()
    }

    override fun stream(request: StoreReadRequest<ScreenplayIds>): StoreFlow<Screenplay> =
        screenplay?.let(::storeFlowOf) ?: storeFlowOf(NetworkError.Unknown)
}
