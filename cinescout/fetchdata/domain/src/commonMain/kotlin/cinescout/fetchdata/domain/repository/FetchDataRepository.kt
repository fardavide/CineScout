package cinescout.fetchdata.domain.repository

import cinescout.fetchdata.domain.model.Bookmark
import cinescout.fetchdata.domain.model.FetchData
import cinescout.fetchdata.domain.model.Page
import korlibs.time.DateTime
import kotlin.time.Duration

interface FetchDataRepository {

    suspend fun get(key: Any, expiration: Duration): FetchData?
    
    suspend fun getPage(key: Any, expiration: Duration): Int? {
        val fetchData = get(key, expiration)
        return when (val bookmark = fetchData?.bookmark) {
            is Page -> bookmark.value
            else -> null
        }
    }

    suspend fun set(key: Any) = set(key, Bookmark.None)
    suspend fun set(key: Any, bookmark: Bookmark)
}

class FakeFetchDataRepository(
    data: Map<out Any, FetchData> = emptyMap()
) : FetchDataRepository {

    private val data = data.toMutableMap()

    override suspend fun get(key: Any, expiration: Duration): FetchData? = data[key]

    override suspend fun set(key: Any, bookmark: Bookmark) {
        data[key] = FetchData(DateTime.now(), bookmark)
    }
}
