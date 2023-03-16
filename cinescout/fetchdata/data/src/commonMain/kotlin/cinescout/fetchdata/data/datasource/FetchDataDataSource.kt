package cinescout.fetchdata.data.datasource

import cinescout.database.FetchDataQueries
import cinescout.database.model.DatabaseFetchKey
import cinescout.database.util.suspendTransaction
import cinescout.database.util.suspendTransactionWithResult
import cinescout.utils.kotlin.DispatcherQualifier
import com.soywiz.klock.DateTime
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class FetchDataDataSource(
    private val fetchDataQueries: FetchDataQueries,
    @Named(DispatcherQualifier.Io) private val readDispatcher: CoroutineDispatcher,
    @Named(DispatcherQualifier.DatabaseWrite) private val writeDispatcher: CoroutineDispatcher
) {

    suspend fun get(databaseFetchKey: DatabaseFetchKey): DateTime =
        fetchDataQueries.suspendTransactionWithResult(readDispatcher) {
            find(databaseFetchKey).executeAsOneOrNull() ?: DateTime.EPOCH
        }

    suspend fun set(databaseFetchKey: DatabaseFetchKey, value: DateTime) {
        fetchDataQueries.suspendTransaction(writeDispatcher) {
            insert(databaseFetchKey, value)
        }
    }
}
