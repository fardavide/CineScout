package store

import kotlinx.coroutines.CoroutineDispatcher

interface StoreOwner {

    val dispatcher: CoroutineDispatcher

    suspend fun getFetchData(key: StoreKeyValue): FetchData?

    suspend fun saveFetchData(key: StoreKeyValue, data: FetchData)
}

class RealStoreOwner(
    override val dispatcher: CoroutineDispatcher,
    private val getFetchData: suspend (StoreKeyValue) -> FetchData?,
    private val saveFetchData: suspend (StoreKeyValue, FetchData) -> Unit
) : StoreOwner {

    override suspend fun getFetchData(key: StoreKeyValue): FetchData? =
        getFetchData.invoke(key)

    override suspend fun saveFetchData(key: StoreKeyValue, data: FetchData) {
        saveFetchData.invoke(key, data)
    }
}
