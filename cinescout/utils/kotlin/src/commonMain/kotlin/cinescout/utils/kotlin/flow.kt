package cinescout.utils.kotlin

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

@Suppress("UNCHECKED_CAST")
fun <T1, T2, R> combineLatest(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    @BuilderInference transform: suspend (T1, T2) -> Flow<R>
): Flow<R> = combine(flow, flow2) { args: Array<*> ->
    transform(
        args[0] as T1,
        args[1] as T2
    )
}.flatMapLatest { it }

@Suppress("UNCHECKED_CAST")
fun <T1, T2, T3, R> combineLatest(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    @BuilderInference transform: suspend (T1, T2, T3) -> Flow<R>
): Flow<R> = combine(flow, flow2, flow3) { args: Array<*> ->
    transform(
        args[0] as T1,
        args[1] as T2,
        args[2] as T3
    )
}.flatMapLatest { it }

@Suppress("UNCHECKED_CAST")
fun <T1, T2, T3, T4, R> combineLatest(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    @BuilderInference transform: suspend (T1, T2, T3, T4) -> Flow<R>
): Flow<R> = combine(flow, flow2, flow3, flow4) { args: Array<*> ->
    transform(
        args[0] as T1,
        args[1] as T2,
        args[2] as T3,
        args[3] as T4
    )
}.flatMapLatest { it }

inline fun <reified T> combineToList(flows: Collection<Flow<T>>): Flow<List<T>> =
    if (flows.isEmpty()) flowOf(emptyList())
    else combine(flows) { array -> array.toList() }

inline fun <reified T> List<Flow<T>>.combineToList(): Flow<List<T>> =
    combineToList(this)

inline fun <reified T : Any> combineToLazyList(flows: Collection<Flow<T>>): Flow<List<T>> {
    val lazyFlows = flows.map { flow ->
        flow.map { t ->
            @Suppress("USELESS_CAST", "CastToNullableType")
            t as T?
        }.onStart { emit(null) }
    }
    return combineToList(lazyFlows).map { it.filterNotNull() }.filterNot { it.isEmpty() }
}

inline fun <reified T : Any> List<Flow<T>>.combineToLazyList(): Flow<List<T>> =
    combineToLazyList(this)

fun <T1, T2> combineToPair(flow: Flow<T1>, flow2: Flow<T2>): Flow<Pair<T1, T2>> =
    combine(flow, flow2) { t1, t2 -> t1 to t2 }
