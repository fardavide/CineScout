package cinescout.store5

import cinescout.store5.mapper.mapToStore5ReadResponse
import kotlinx.coroutines.flow.Flow
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreReadRequest

class Store5<Key : Any, Output : Any> internal constructor(private val store: Store<Key, Output>) {

    fun stream(request: StoreReadRequest<Key>): Flow<Store5ReadResponse<Output>> =
        store.stream(request).mapToStore5ReadResponse()
}
