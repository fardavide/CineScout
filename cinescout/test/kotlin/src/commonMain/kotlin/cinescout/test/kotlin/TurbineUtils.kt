package cinescout.test.kotlin

import app.cash.turbine.Event
import app.cash.turbine.ReceiveTurbine

suspend fun <T> ReceiveTurbine<T>.awaitLastItem(): T =
    cancelAndConsumeRemainingEvents().filterIsInstance<Event.Item<T>>().last().value
