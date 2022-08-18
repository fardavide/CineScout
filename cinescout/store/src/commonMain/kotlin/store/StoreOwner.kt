package store

import kotlinx.coroutines.CoroutineDispatcher

interface StoreOwner {

    val dispatcher: CoroutineDispatcher

    suspend fun findFetchData(key: StoreKey): FetchData?

    suspend fun insertFetchData(key: StoreKey, data: FetchData)
}

class RealStoreOwner(
    override val dispatcher: CoroutineDispatcher,
    private val findFetchData: suspend (StoreKey) -> FetchData?,
    private val insertFetchData: suspend (StoreKey, FetchData) -> Unit
) : StoreOwner {

    override suspend fun findFetchData(key: StoreKey): FetchData? =
        findFetchData.invoke(key)

    override suspend fun insertFetchData(key: StoreKey, data: FetchData) {
        insertFetchData.invoke(key, data)
    }
}
