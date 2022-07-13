package cinescout.utils.kotlin

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

fun <T1, T2> combineToPair(flow: Flow<T1>, flow2: Flow<T2>): Flow<Pair<T1, T2>> =
    combine(flow, flow2) { t1, t2 -> t1 to t2 }
