package cinescout.store

import cinescout.database.StoreFetchDataQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import store.FetchData
import store.StoreKey
import store.StoreKeyValue

internal class StoreFetchDataRepository(
    private val ioDispatcher: CoroutineDispatcher,
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
