package cinescout.fetchdata.domain.repository

import cinescout.fetchdata.domain.model.FetchData
import kotlin.time.Duration

interface FetchDataRepository {

    suspend fun get(key: Any, expiration: Duration): FetchData?
    
    suspend fun getPage(key: Any, expiration: Duration): Int? = get(key, expiration)?.page

    suspend fun set(key: Any, page: Int = 0)
}
