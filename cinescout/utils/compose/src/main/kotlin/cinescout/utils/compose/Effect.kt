package cinescout.utils.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope

/**
 * This is a container for single-use state.
 * Use this when you don't want an event to be repeated, for example while emitting an error to the ViewModel
 *
 * You usually want to consume this into a [LaunchedEffect] or [Consume] block
 */
class Effect<T : Any> private constructor(internal var event: T?) {

    /**
     * @return the [event] if not consumed, `null` otherwise
     */
    fun consume(): T? = event
        .also { event = null }

    override fun equals(other: Any?) = other is Effect<*> && event == other.event

    override fun hashCode() = event.hashCode()

    override fun toString() = "Effect($event)"

    companion object {

        fun <T : Any> of(event: T) = Effect(event)
        fun <T : Any> empty() = Effect<T>(null)
    }
}

/**
 * Executes [block] in the scope of [effect]
 * @param block will be called only when there is an [Effect.event] to consume
 */
@Composable
inline fun <T : Any> Consume(effect: Effect<T>, block: (T) -> Unit) {
    effect.consume()?.let { event ->
        block(event)
    }
}

/**
 * Executes a [LaunchedEffect] in the scope of [effect]
 * @param block will be called only when there is an [Effect.event] to consume
 */
@Composable
inline fun <T : Any> ConsumableLaunchedEffect(
    effect: Effect<T>,
    crossinline block: suspend CoroutineScope.(T) -> Unit
) {
    LaunchedEffect(effect) {
        effect.consume()?.let { event ->
            block(event)
        }
    }
}

