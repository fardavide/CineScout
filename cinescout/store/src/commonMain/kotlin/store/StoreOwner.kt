package store

interface StoreOwner {

    suspend fun getFetchData(key: StoreKeyValue): FetchData?

    suspend fun saveFetchData(key: StoreKeyValue, data: FetchData)
}

class RealStoreOwner(
    private val getFetchData: suspend (StoreKeyValue) -> FetchData?,
    private val saveFetchData: suspend (StoreKeyValue, FetchData) -> Unit
) : StoreOwner {

    override suspend fun getFetchData(key: StoreKeyValue): FetchData? =
        getFetchData.invoke(key)

    override suspend fun saveFetchData(key: StoreKeyValue, data: FetchData) {
        saveFetchData.invoke(key, data)
    }
}
