package cinescout.store

import cinescout.database.StoreFetchDataQueries
import cinescout.utils.kotlin.DispatcherQualifier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import store.FetchData
import store.StoreKeyValue

@Factory
internal class StoreFetchDataRepository(
    @Named(DispatcherQualifier.Io) private val ioDispatcher: CoroutineDispatcher,
    private val queries: StoreFetchDataQueries
) {

    suspend fun getFetchData(key: StoreKeyValue): FetchData? =
        withContext(ioDispatcher) {
            queries.find(key.value).executeAsOneOrNull()?.let { storeFetchData ->
                FetchData(storeFetchData.dateTime)
            }
        }

    suspend fun saveFetchData(key: StoreKeyValue, fetchData: FetchData) {
        withContext(ioDispatcher) {
            queries.insert(key.value, fetchData.dateTime)
        }
    }
}
