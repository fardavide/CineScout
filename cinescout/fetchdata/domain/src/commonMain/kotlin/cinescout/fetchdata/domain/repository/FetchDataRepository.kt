package cinescout.fetchdata.domain.repository

import cinescout.fetchdata.domain.model.FetchData
import com.soywiz.klock.DateTime
import kotlin.time.Duration

interface FetchDataRepository {

    suspend fun get(key: Any, expiration: Duration): FetchData?
    
    suspend fun getPage(key: Any, expiration: Duration): Int? = get(key, expiration)?.page

    suspend fun set(key: Any, page: Int = 0)
}

class FakeFetchDataRepository(
    data: Map<out Any, FetchData> = emptyMap()
) : FetchDataRepository {

    private val data = data.toMutableMap()

    override suspend fun get(key: Any, expiration: Duration): FetchData? = data[key]

    override suspend fun set(key: Any, page: Int) {
        data[key] = FetchData(DateTime.now(), page)
    }
}
