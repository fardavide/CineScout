package cinescout.test.kotlin

import app.cash.turbine.Event
import app.cash.turbine.ReceiveTurbine
import cinescout.utils.kotlin.findInstance

suspend fun <T> ReceiveTurbine<T>.awaitLastItem(): T {
    val events = cancelAndConsumeRemainingEvents()
    events.findInstance<Event.Error>()?.let { throw it.throwable }
    val items = events.filterIsInstance<Event.Item<T>>()
    return items.last().value
}
