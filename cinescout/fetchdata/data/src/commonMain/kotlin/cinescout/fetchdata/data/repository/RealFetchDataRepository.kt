package cinescout.fetchdata.data.repository

import cinescout.fetchdata.data.datasource.FetchDataDataSource
import cinescout.fetchdata.data.mapper.DatabaseFetchKeyMapper
import cinescout.fetchdata.domain.model.FetchKey
import cinescout.fetchdata.domain.repository.FetchDataRepository
import com.soywiz.klock.DateTime
import org.koin.core.annotation.Factory

@Factory
internal class RealFetchDataRepository(
    private val dataSource: FetchDataDataSource,
    private val mapper: DatabaseFetchKeyMapper
) : FetchDataRepository {

    override suspend fun get(key: FetchKey): DateTime {
        val databaseKey = mapper.toDatabaseFetchKey(key)
        return dataSource.get(databaseKey)
    }

    override suspend fun set(key: FetchKey, value: DateTime) {
        val databaseKey = mapper.toDatabaseFetchKey(key)
        dataSource.set(databaseKey, value)
    }
}
