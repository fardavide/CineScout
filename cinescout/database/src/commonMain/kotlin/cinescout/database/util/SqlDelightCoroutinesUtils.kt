package cinescout.database.util

import app.cash.sqldelight.Query
import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import cinescout.utils.kotlin.nonEmpty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

fun <T : Any> Flow<Query<T>>.mapToListOrError(
    context: CoroutineContext = Dispatchers.Default
): Flow<Either<DataError.Local.NoCache, NonEmptyList<T>>> = map {
    withContext(context) {
        it.executeAsList().nonEmpty { DataError.Local.NoCache }
    }
}

fun <T : Any> Flow<Query<T>>.mapToOneOrError(
    context: CoroutineContext = Dispatchers.Default
): Flow<Either<DataError.Local, T>> = map {
    withContext(context) {
        it.executeAsOneOrNull()?.right() ?: DataError.Local.NoCache.left()
    }
}
