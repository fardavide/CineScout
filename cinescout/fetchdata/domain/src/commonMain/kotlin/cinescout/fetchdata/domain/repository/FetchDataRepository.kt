package cinescout.fetchdata.domain.repository

import cinescout.fetchdata.domain.model.FetchData
import kotlin.time.Duration

interface FetchDataRepository {

    suspend fun get(key: Any, expiration: Duration): FetchData?

    suspend fun set(key: Any, fetchData: FetchData)
}
