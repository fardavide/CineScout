package database

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.db.SqlCursor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.context.KoinContextHandler.get
import org.koin.core.error.NoBeanDefFoundException
import util.DispatchersProvider
import kotlin.coroutines.CoroutineContext

suspend fun <T : Any> Query<T>.suspend(context: CoroutineContext = Io): SqlCursor =
    withContext(context) { execute() }

suspend fun <T : Any> Query<T>.suspendAsOne(context: CoroutineContext = Io): T =
    withContext(context) { executeAsOne() }

suspend fun <T : Any> Query<T>.suspendAsOneOrNull(context: CoroutineContext = Io): T? =
    withContext(context) { executeAsOneOrNull() }

suspend fun <T : Any> Query<T>.suspendAsList(context: CoroutineContext = Io): List<T> =
    withContext(context) { executeAsList() }


private val Io: CoroutineDispatcher by lazy {
    try {
        get().get<DispatchersProvider>().Io
    } catch (ignored: NoBeanDefFoundException) {
        // Run on Main for tests
        Dispatchers.Main
    }
}
