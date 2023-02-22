package cinescout.utils.kotlin

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlin.time.Duration

fun <T> ticker(interval: Duration, @BuilderInference block: suspend FlowCollector<T>.() -> Unit): Flow<T> =
    flow {
        while (currentCoroutineContext().isActive) {
            block()
            delay(interval)
        }
    }
