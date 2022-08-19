package store

import kotlinx.coroutines.CoroutineDispatcher

interface StoreOwner {

    val dispatcher: CoroutineDispatcher

    suspend fun getFetchData(key: StoreKey): FetchData?

    suspend fun saveFetchData(key: StoreKey, data: FetchData)
}

class RealStoreOwner(
    override val dispatcher: CoroutineDispatcher,
    private val getFetchData: suspend (StoreKey) -> FetchData?,
    private val saveFetchData: suspend (StoreKey, FetchData) -> Unit
) : StoreOwner {

    override suspend fun getFetchData(key: StoreKey): FetchData? =
        getFetchData.invoke(key)

    override suspend fun saveFetchData(key: StoreKey, data: FetchData) {
        saveFetchData.invoke(key, data)
    }
}
