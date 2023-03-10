package cinescout.store5.test

import cinescout.store5.StoreFlow
import cinescout.store5.ext.fetcherResponseDataOf
import kotlinx.coroutines.flow.flowOf

fun <Output : Any> storeFlowOf(head: Output, vararg tail: Output): StoreFlow<Output> = flowOf(
    fetcherResponseDataOf(head),
    *tail.map(::fetcherResponseDataOf).toTypedArray()
)
