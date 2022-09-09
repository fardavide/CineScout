package cinescout.utils.kotlin

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlin.time.Duration

fun <T> ticker(interval: Duration, block: suspend FlowCollector<T>.() -> Unit) = flow {
    while (currentCoroutineContext().isActive) {
        block()
        delay(interval)
    }
}
