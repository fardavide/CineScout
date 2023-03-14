package cinescout.store5

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.store5.mapper.mapToStore5ReadResponse
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import org.mobilenativefoundation.store.store5.ExperimentalStoreApi
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface Store5<Key : Any, Output : Any> {

    suspend fun clear()

    suspend fun fresh(key: Key): Either<NetworkError, Output> = stream(StoreReadRequest.fresh(key))
        .filterIsInstance<Store5ReadResponse.Data<Output>>()
        .first()
        .value

    suspend fun get(key: Key): Either<NetworkError, Output> =
        stream(StoreReadRequest.cached(key, refresh = false))
            .filterIsInstance<Store5ReadResponse.Data<Output>>()
            .first()
            .value
    
    fun stream(request: StoreReadRequest<Key>): StoreFlow<Output>
}

suspend fun <Output : Any> Store5<Unit, Output>.fresh(): Either<NetworkError, Output> = fresh(Unit)

fun <Output : Any> Store5<Unit, Output>.stream(refresh: Boolean): StoreFlow<Output> =
    stream(StoreReadRequest.cached(Unit, refresh))

internal class RealStore5<Key : Any, Output : Any>(
    private val store: Store<Key, Output>
) : Store5<Key, Output> {

    @OptIn(ExperimentalStoreApi::class)
    override suspend fun clear() {
        store.clear()
    }
    
    override fun stream(request: StoreReadRequest<Key>): StoreFlow<Output> =
        store.stream(request).mapToStore5ReadResponse()
}
