package cinescout.fetchdata.data.repository

import cinescout.GetCurrentDateTime
import cinescout.fetchdata.data.datasource.FetchDataDataSource
import cinescout.fetchdata.domain.model.FetchData
import cinescout.fetchdata.domain.repository.FetchDataRepository
import cinescout.utils.kotlin.toTimeSpan
import org.koin.core.annotation.Factory
import kotlin.time.Duration

@Factory
internal class RealFetchDataRepository(
    private val dataSource: FetchDataDataSource,
    private val getCurrentDateTime: GetCurrentDateTime
) : FetchDataRepository {

    override suspend fun get(key: Any, expiration: Duration): FetchData? =
        dataSource.get(key)?.takeIf { it.isExpired(expiration).not() }

    override suspend fun set(key: Any, fetchData: FetchData) {
        dataSource.set(key, fetchData.page, fetchData.dateTime)
    }

    private fun FetchData.isExpired(expiration: Duration) =
        this@RealFetchDataRepository.getCurrentDateTime() - dateTime > expiration.toTimeSpan()
}
