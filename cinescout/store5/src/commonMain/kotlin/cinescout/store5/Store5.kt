package cinescout.store5

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.store5.mapper.mapToStore5ReadResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface Store5<Key : Any, Output : Any> {

    suspend fun fresh(key: Key): Either<NetworkError, Output> = stream(StoreReadRequest.fresh(key))
        .filterIsInstance<Store5ReadResponse.Data<Output>>()
        .first()
        .value

    suspend fun get(key: Key): Either<NetworkError, Output> =
        stream(StoreReadRequest.cached(key, refresh = false))
            .filterIsInstance<Store5ReadResponse.Data<Output>>()
            .first()
            .value
    
    fun stream(request: StoreReadRequest<Key>): Flow<Store5ReadResponse<Output>>
}

internal class RealStore5<Key : Any, Output : Any>(
    private val store: Store<Key, Output>
) : Store5<Key, Output> {
    
    override fun stream(request: StoreReadRequest<Key>): Flow<Store5ReadResponse<Output>> =
        store.stream(request).mapToStore5ReadResponse()
}
