package store.test

import com.soywiz.klock.DateTime
import kotlinx.coroutines.CoroutineDispatcher
import store.FetchData
import store.StoreKeyValue
import store.StoreOwner

class MockStoreOwner(override val dispatcher: CoroutineDispatcher) : StoreOwner {

    private var fetchData: FetchData? = null

    fun expired(): StoreOwner = apply {
        fetchData = FetchData(DateTime.EPOCH)
    }

    fun fresh(): StoreOwner = apply {
        fetchData = null
    }

    fun updated(): StoreOwner = apply {
        fetchData = FetchData(DateTime.now())
    }

    override suspend fun getFetchData(key: StoreKeyValue) = fetchData

    override suspend fun saveFetchData(key: StoreKeyValue, data: FetchData) {
        this.fetchData = data
    }
}
