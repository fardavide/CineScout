package cinescout.test.kotlin

import app.cash.turbine.Event
import app.cash.turbine.ReceiveTurbine
import cinescout.utils.kotlin.findInstance

suspend fun <T> ReceiveTurbine<T>.expectLastItem(): T {
    val events = cancelAndConsumeRemainingEvents()
    events.findInstance<Event.Error>()?.let { throw it.throwable }
    val items = events.filterIsInstance<Event.Item<T>>()
    return items.last().value
}

suspend fun <T> ReceiveTurbine<T>.expectItem(): T = when (val event = awaitEvent()) {
    Event.Complete -> error("Expected an item but the flow has completed.")
    is Event.Error -> throw event.throwable
    is Event.Item -> event.value
}
