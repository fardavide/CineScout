package cinescout.store

import cinescout.database.StoreFetchDataQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import store.FetchData
import store.StoreKey

internal class StoreFetchDataRepository(
    private val ioDispatcher: CoroutineDispatcher,
    private val queries: StoreFetchDataQueries
) {

    suspend fun getFetchData(key: StoreKey): FetchData? =
        withContext(ioDispatcher) {
            queries.find(key.value).executeAsOneOrNull()?.let { storeFetchData ->
                FetchData(storeFetchData.dateTime)
            }
        }

    suspend fun saveFetchData(key: StoreKey, fetchData: FetchData) {
        withContext(ioDispatcher) {
            queries.insert(key.value, fetchData.dateTime)
        }
    }
}
