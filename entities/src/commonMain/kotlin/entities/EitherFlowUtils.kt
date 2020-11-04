package entities

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import util.unsupported

/**
 * Easily [Flow.map] a [Flow] of [Either]
 */
inline fun <A1, B1, A, B> Flow<Either<A1, B1>>.foldMap(
    crossinline ifLeft: (A1) -> A,
    crossinline ifRight: (B1) -> B
): Flow<Either<A, B>> = map { either ->
    when {
        either.isLeft() -> ifLeft(either.leftOrThrow()).left()
        either.isRight() -> ifRight(either.rightOrThrow()).right()
        else -> unsupported
    }
}

/**
 * Terminate the flow when a [Either.Left] is emitted
 */
fun <A, B> Flow<Either<A, B>>.fix(): Flow<Either<A, B>> =
    fixedFlatMapLatest(::flowOf).first

/**
 * Merge receiver [Flow] with [other] after the first one is completed
 * E.G.
 * { 1, 2, 3 }
 *   +
 * { 4, 5 }
 *   =
 * { 1, 2, 3, 4, 5 }
 *
 * @param other the [Flow] that will be emitted after the receiver is completed
 */
operator fun <A, B> Flow<Either<A, B>>.plus(other: Flow<Either<A, B>>) =
    plus { other }

/**
 * Merge receiver [Flow] with [other] after the first one is completed
 * E.G.
 * { 1, 2, 3 }
 *   +
 * { 4, 5 }
 *   =
 * { 1, 2, 3, 4, 5 }
 *
 * @param other a lambada that returns the [Flow] that will be emitted after the receiver is completed.
 *   Differently from the variant that receives a [Flow] directly, this enables us to launch lazily the functions that
 *   returns the [Flow].
 *   In a real case scenario, we can use `login() + { sync() }` where `sync` will be executed only after `login` is
 *   completed.
 */
operator fun <A, B> Flow<Either<A, B>>.plus(other: () -> Flow<Either<A, B>>): Flow<Either<A, B>> {
    val (flow, hasLeft) = fixedFlatMapLatest { either ->
        flowOf(either)
    }
    return flow.onCompletion {
        if (hasLeft().not()) emitAll(other().fix())
    }
}

/**
 * Concatenate the receiver [Flow] with the [next] [Flow] when the receiver emits [terminal]
 * In most of the cases, is suggested to use `plus`, then only differences is that with [then] we can specify a
 * terminal value, but Flow builders are smart enough for indicate when a Flow is completed
 *
 * @param terminal then [B] that declare when the first flow is completed
 * @param next the [Flow] that will be emitted after the receiver emits [terminal]
 */
fun <A, B> Flow<Either<A, B>>.then(
    terminal: B,
    next: Flow<Either<A, B>>
): Flow<Either<A, B>> =
    then(terminal) { next }

/**
 * Concatenate the receiver [Flow] with the [next] [Flow] when the receiver emits [terminal]
 * In most of the cases, is suggested to use `plus`, then only differences is that with [then] we can specify a
 * terminal value, but Flow builders are smart enough for indicate when a Flow is completed
 *
 * @param terminal then [B] that declare when the first flow is completed
 * @param next a lambada that returns the [Flow] that will be emitted after the receiver is completed.
 *   Differently from the variant that receives a [Flow] directly, this enables us to launch lazily the functions that
 *   returns the [Flow].
 *   In a real case scenario, we can use `login().then(LoginState.Completed) { sync() }` where `sync` will be executed
 *   only after `login` emits `LoginState.Completed`.
 */
inline fun <A, B> Flow<Either<A, B>>.then(
    terminal: B,
    crossinline next: (B) -> Flow<Either<A, B>>
): Flow<Either<A, B>> {
    var hasFirstCompleted = false
    return fixedFlatMapLatest { either ->

        if (hasFirstCompleted) {
            flowOf()

        } else {
            if (either.rightOrNull() == terminal) {
                hasFirstCompleted = true
                flowOf(either) + next(either.rightOrThrow()).fix()

            } else flowOf(either)
        }
    }.first
}

/**
 * Execute a [Flow.flatMapLatest] with short circuit of [Either.Left] values
 * @return [Pair] of flatMap result of [Flow] and lambda that returns a [Boolean] indicating whether the [Flow] has
 *   emitted any [Either.Left]
 *
 * Example:
 * ```kotlin
val (flow, getHasLeft) = intFlow.fixedFlatMapLatest { flowOf(it * 2) }
val hasLeft = getHasLeft()
 * ```
 */
@PublishedApi
internal inline fun <A1, B1, T: Either<A1, B1>, A2, B2, R: Either<A2, B2>> Flow<T>.fixedFlatMapLatest(
    crossinline transform: (T) -> Flow<R>
): Pair<Flow<R>, () -> Boolean> {
    var hasLeft = false
    return flatMapLatest {
        if (hasLeft.not()) {
            hasLeft = it.isLeft()
            transform(it)
        } else flowOf()
    } to { hasLeft }
}
