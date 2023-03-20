package cinescout.store5

import arrow.core.Either
import arrow.core.left
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.store5.mapper.mapToStore5ReadResponse
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.transform
import org.mobilenativefoundation.store.store5.ExperimentalStoreApi
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface Store5<Key : Any, Output : Any> {

    suspend fun clear()

    suspend fun fresh(key: Key): Either<NetworkError, Output> = stream(StoreReadRequest.fresh(key))
        .filterIsInstance<Store5ReadResponse.Data<Output>>()
        .first()
        .value

    suspend fun freshAsOperation(key: Key): Either<NetworkOperation, Output> =
        stream(StoreReadRequest.fresh(key)).transform { response ->
            when (response) {
                is Store5ReadResponse.Data -> emit(response.value.mapLeft(NetworkOperation::Error))
                is Store5ReadResponse.Loading -> Unit
                Store5ReadResponse.Skipped -> emit(NetworkOperation.Skipped.left())
            }
        }.first()

    suspend fun get(key: Key): Either<NetworkError, Output> =
        stream(StoreReadRequest.cached(key, refresh = false))
            .filterIsInstance<Store5ReadResponse.Data<Output>>()
            .first()
            .value
    
    fun stream(request: StoreReadRequest<Key>): StoreFlow<Output>
}

suspend fun <Output : Any> Store5<Unit, Output>.fresh(): Either<NetworkError, Output> = fresh(Unit)

suspend fun <Output : Any> Store5<Unit, Output>.freshAsOperation(): Either<NetworkOperation, Output> =
    freshAsOperation(Unit)

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
