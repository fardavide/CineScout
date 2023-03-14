package cinescout.store5

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.store5.mapper.mapToStore5ReadResponse
import cinescout.store5.mapper.toEither
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import org.mobilenativefoundation.store.store5.ExperimentalStoreApi
import org.mobilenativefoundation.store.store5.MutableStore
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreWriteRequest

interface MutableStore5<Key : Any, Output : Any, Response : Any> {

    suspend fun clear(key: Key)

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

    suspend fun write(request: StoreWriteRequest<Key, Output, Response>): Either<NetworkError, Response>
}

suspend fun <O : Any, R : Any> MutableStore5<Unit, O, R>.fresh(): Either<NetworkError, O> = fresh(Unit)

fun <Output : Any, Response : Any> MutableStore5<Unit, Output, Response>.stream(
    refresh: Boolean
): StoreFlow<Output> = stream(StoreReadRequest.cached(Unit, refresh))

suspend fun <Output : Any, Response : Any> MutableStore5<Unit, Output, Response>.write(
    value: Output
): Either<NetworkError, Response> = write(StoreWriteRequest.of(Unit, value))

internal class RealMutableStore5<Key : Any, Output : Any, Response : Any>(
    private val store: MutableStore<Key, Output>
) : MutableStore5<Key, Output, Response> {

    override suspend fun clear(key: Key) {
        store.clear(key)
    }

    override fun stream(request: StoreReadRequest<Key>): StoreFlow<Output> =
        store.stream<Response>(request).mapToStore5ReadResponse()

    @OptIn(ExperimentalStoreApi::class)
    override suspend fun write(
        request: StoreWriteRequest<Key, Output, Response>
    ): Either<NetworkError, Response> = store.write(request).toEither()
}
