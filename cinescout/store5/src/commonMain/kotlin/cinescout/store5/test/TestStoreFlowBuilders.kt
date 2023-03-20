package cinescout.store5.test

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.store5.Store5ReadResponse
import cinescout.store5.StoreFlow
import cinescout.store5.ext.fetcherResponseDataOf
import kotlinx.coroutines.flow.flowOf

fun <Output : Any> storeFlowOf(head: Output, vararg tail: Output): StoreFlow<Output> = flowOf(
    fetcherResponseDataOf(head),
    *tail.map(::fetcherResponseDataOf).toTypedArray()
)

fun <Output : Any> storeFlowOf(
    head: Store5ReadResponse<Output>,
    vararg tail: Store5ReadResponse<Output>
): StoreFlow<Output> = flowOf(head, *tail)

fun <Output : Any> storeFlowOf(error: NetworkError): StoreFlow<Output> = flowOf(
    fetcherResponseDataOf(error)
)

fun <Output : Any> storeFlowOf(either: Either<NetworkError, Output>): StoreFlow<Output> = flowOf(
    fetcherResponseDataOf(either)
)
