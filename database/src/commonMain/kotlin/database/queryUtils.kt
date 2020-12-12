package database

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.db.SqlCursor
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import entities.Either
import entities.Error
import entities.MissingCache
import entities.ResourceError
import entities.left
import entities.right
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.core.context.KoinContextHandler
import org.koin.core.error.NoBeanDefFoundException
import util.DispatchersProvider
import util.takeIfNotEmpty
import kotlin.coroutines.CoroutineContext

// Suspend
suspend fun <T : Any> Query<T>.suspend(context: CoroutineContext = Io): SqlCursor =
    withContext(context) { execute() }

suspend fun <T : Any> Query<T>.suspendAsOne(context: CoroutineContext = Io): T =
    withContext(context) { executeAsOne() }

suspend fun <T : Any> Query<T>.suspendAsOneOrNull(context: CoroutineContext = Io): T? =
    withContext(context) { executeAsOneOrNull() }

suspend fun <T : Any> Query<T>.suspendAsOneOrError(context: CoroutineContext = Io): Either<MissingCache, T> =
    suspendAsOneOrNull(context)?.right() ?: MissingCache.left()

suspend fun <T : Any> Query<T>.suspendAsList(context: CoroutineContext = Io): List<T> =
    withContext(context) { executeAsList() }

// Flow
fun <T : Any> Query<T>.asFlowOfList(
    context: CoroutineContext = Io
): Flow<List<T>> =
    asFlow().mapToList(context)

fun <T : Any> Query<T>.asFlowOfOneOrError(
    context: CoroutineContext = Io
): Flow<Either<MissingCache, T>> =
    asFlowOfOneOrError(context) { it }

fun <T : Any> Query<T>.asFlowOfOneOrResourceError(
    context: CoroutineContext = Io
): Flow<Either<ResourceError.Local, T>> =
    asFlowOfOneOrError(context, ResourceError::Local)

fun <T : Any, E : Error> Query<T>.asFlowOfOneOrError(
    context: CoroutineContext = Dispatchers.Default,
    onError: (MissingCache) -> E
): Flow<Either<E, T>> =
    asFlow().mapToOneOrNull(context)
        .map { it?.right() ?: onError(MissingCache).left() }

fun <T : Any> Query<T>.asFlowOfListOrResourceError(
    context: CoroutineContext = Dispatchers.Default,
): Flow<Either<ResourceError, List<T>>> =
    asFlowOfListOrError(context, ResourceError::Local)

fun <T : Any, E : Error> Query<T>.asFlowOfListOrError(
    context: CoroutineContext = Dispatchers.Default,
    onError: (MissingCache) -> E
): Flow<Either<E, List<T>>> =
    asFlow().mapToList(context)
        .map { it.takeIfNotEmpty()?.right() ?: onError(MissingCache).left() }


private val Io: CoroutineDispatcher by lazy {
    try {
        KoinContextHandler.getOrNull()
            ?.get<DispatchersProvider>()?.Io
            // Run on Main for tests without Koin
            ?: Dispatchers.Main

    } catch (ignored: NoBeanDefFoundException) {
        // Run on Main for tests without DispatcherProvider
        Dispatchers.Main
    }
}
