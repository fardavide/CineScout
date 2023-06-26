package cinescout.screenplay.domain.store

import cinescout.CineScoutTestApi
import cinescout.error.NetworkError
import cinescout.notImplementedFake
import cinescout.screenplay.domain.model.ScreenplayWithGenreSlugs
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.store5.Store5
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface ScreenplayStore : Store5<ScreenplayIds, ScreenplayWithGenreSlugs>

@CineScoutTestApi
class FakeScreenplayStore(
    private val screenplay: ScreenplayWithGenreSlugs? = null
) : ScreenplayStore {

    override suspend fun clear() {
        notImplementedFake()
    }

    override fun stream(request: StoreReadRequest<ScreenplayIds>): StoreFlow<ScreenplayWithGenreSlugs> =
        screenplay?.let(::storeFlowOf) ?: storeFlowOf(NetworkError.Unknown)
}
