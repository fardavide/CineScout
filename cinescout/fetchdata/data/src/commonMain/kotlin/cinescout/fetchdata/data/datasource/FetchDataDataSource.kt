package cinescout.fetchdata.data.datasource

import cinescout.database.FetchDataQueries
import cinescout.database.util.suspendTransaction
import cinescout.database.util.suspendTransactionWithResult
import cinescout.fetchdata.data.mapper.FetchDataKeyMapper
import cinescout.fetchdata.domain.model.FetchData
import cinescout.utils.kotlin.DatabaseWriteDispatcher
import cinescout.utils.kotlin.IoDispatcher
import korlibs.time.DateTime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

internal interface FetchDataDataSource {

    suspend fun get(key: Any): FetchData?

    suspend fun set(
        key: Any,
        page: Int,
        dateTime: DateTime
    )
}

@Factory
internal class RealFetchDataDataSource(
    private val keyMapper: FetchDataKeyMapper,
    private val fetchDataQueries: FetchDataQueries,
    @Named(IoDispatcher) private val readDispatcher: CoroutineDispatcher,
    @Named(DatabaseWriteDispatcher) private val writeDispatcher: CoroutineDispatcher
) : FetchDataDataSource {

    override suspend fun get(key: Any): FetchData? =
        fetchDataQueries.suspendTransactionWithResult(readDispatcher) {
            find(keyMapper.toDatabaseKey(key), ::toFetchData).executeAsOneOrNull()
        }

    override suspend fun set(
        key: Any,
        page: Int,
        dateTime: DateTime
    ) {
        fetchDataQueries.suspendTransaction(writeDispatcher) {
            insert(keyMapper.toDatabaseKey(key), page, dateTime)
        }
    }

    private fun toFetchData(page: Int, dateTime: DateTime) = FetchData(dateTime, page)
}

internal class FakeFetchDataDataSource(fetchDataMap: Map<out Any, FetchData> = emptyMap()) : FetchDataDataSource {

    private val mutableFetchData = MutableStateFlow(fetchDataMap)

    override suspend fun get(key: Any): FetchData? = mutableFetchData.value[key]

    override suspend fun set(
        key: Any,
        page: Int,
        dateTime: DateTime
    ) {
        mutableFetchData.emit(mutableFetchData.value + (key to FetchData(dateTime, page)))
    }
}
