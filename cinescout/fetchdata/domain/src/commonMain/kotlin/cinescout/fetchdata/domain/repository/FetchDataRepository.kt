package cinescout.fetchdata.domain.repository

import cinescout.fetchdata.domain.model.FetchKey
import com.soywiz.klock.DateTime

interface FetchDataRepository {

    suspend fun get(key: FetchKey): DateTime

    suspend fun set(key: FetchKey, value: DateTime)
}
