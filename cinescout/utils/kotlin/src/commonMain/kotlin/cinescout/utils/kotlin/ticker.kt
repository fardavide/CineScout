package cinescout.utils.kotlin

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlin.experimental.ExperimentalTypeInference
import kotlin.time.Duration

@OptIn(ExperimentalTypeInference::class)
fun <T> ticker(interval: Duration, @BuilderInference block: suspend FlowCollector<T>.() -> Unit) = flow {
    while (currentCoroutineContext().isActive) {
        block()
        delay(interval)
    }
}
