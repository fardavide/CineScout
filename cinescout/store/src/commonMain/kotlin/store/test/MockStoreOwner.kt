package store.test

import com.soywiz.klock.DateTime
import store.FetchData
import store.StoreKeyValue
import store.StoreOwner

class MockStoreOwner : StoreOwner {

    private var mode = Mode.Fresh
    private val fetchData = mutableMapOf<StoreKeyValue, FetchData>()

    fun fresh(): StoreOwner = apply {
        mode = Mode.Fresh
    }

    fun expired(): StoreOwner = apply {
        mode = Mode.Expired
    }

    fun updated(): StoreOwner = apply {
        mode = Mode.Updated
    }

    override suspend fun getFetchData(key: StoreKeyValue): FetchData? =
        when (mode) {
            Mode.Fresh -> fetchData[key]
            Mode.Expired -> FetchData(DateTime.EPOCH)
            Mode.Updated -> FetchData(DateTime.now())
        }

    override suspend fun saveFetchData(key: StoreKeyValue, data: FetchData) {
        fetchData[key] = data
    }

    private enum class Mode {

        Fresh, Expired, Updated
    }
}
