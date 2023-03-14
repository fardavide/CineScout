package cinescout.screenplay.domain.store

import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.store5.Store5
import cinescout.store5.StoreFlow
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface ScreenplayStore : Store5<TmdbScreenplayId, Screenplay>

class FakeScreenplayStore : ScreenplayStore {

    override suspend fun clear() {
        TODO("Not yet implemented")
    }

    override fun stream(request: StoreReadRequest<TmdbScreenplayId>): StoreFlow<Screenplay> {
        TODO("Not yet implemented")
    }
}
