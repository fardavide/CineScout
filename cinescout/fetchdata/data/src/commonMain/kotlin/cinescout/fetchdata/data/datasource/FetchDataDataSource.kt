package cinescout.fetchdata.data.datasource

import cinescout.CineScoutTestApi
import cinescout.database.FetchDataQueries
import cinescout.database.model.DatabaseBookmark
import cinescout.database.util.suspendTransaction
import cinescout.database.util.suspendTransactionWithResult
import cinescout.fetchdata.data.mapper.DatabaseBookmarkMapper
import cinescout.fetchdata.data.mapper.DatabaseFetchDataKeyMapper
import cinescout.fetchdata.domain.model.Bookmark
import cinescout.fetchdata.domain.model.FetchData
import cinescout.fetchdata.domain.model.Page
import cinescout.utils.kotlin.DatabaseWriteDispatcher
import cinescout.utils.kotlin.IoDispatcher
import korlibs.time.DateTime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

internal interface FetchDataDataSource {

    suspend fun get(key: Any): FetchData?

    @Deprecated(
        "Use with Page instead",
        ReplaceWith("set(key, Page(page))", "cinescout.fetchdata.domain.model.Page")
    )
    suspend fun set(
        key: Any,
        page: Int,
        dateTime: DateTime
    ) {
        set(key, Page(page), dateTime)
    }

    suspend fun set(
        key: Any,
        bookmark: Bookmark,
        dateTime: DateTime
    )
}

@Factory
internal class RealFetchDataDataSource(
    private val bookmarkMapper: DatabaseBookmarkMapper,
    private val fetchDataQueries: FetchDataQueries,
    private val keyMapper: DatabaseFetchDataKeyMapper,
    @Named(IoDispatcher) private val readDispatcher: CoroutineDispatcher,
    @Named(DatabaseWriteDispatcher) private val writeDispatcher: CoroutineDispatcher
) : FetchDataDataSource {

    override suspend fun get(key: Any): FetchData? =
        fetchDataQueries.suspendTransactionWithResult(readDispatcher) {
            find(keyMapper.toDatabaseKey(key), ::toFetchData).executeAsOneOrNull()
        }

    override suspend fun set(
        key: Any,
        bookmark: Bookmark,
        dateTime: DateTime
    ) {
        fetchDataQueries.suspendTransaction(writeDispatcher) {
            insert(keyMapper.toDatabaseKey(key), bookmarkMapper.toDatabaseBookmark(bookmark), dateTime)
        }
    }

    private fun toFetchData(bookmark: DatabaseBookmark, dateTime: DateTime): FetchData {
        bookmarkMapper.toBookmark(bookmark)
        return FetchData(dateTime, bookmarkMapper.toBookmark(bookmark))
    }
}

@CineScoutTestApi
internal class FakeFetchDataDataSource(fetchDataMap: Map<out Any, FetchData> = emptyMap()) : FetchDataDataSource {

    private val mutableFetchData = MutableStateFlow(fetchDataMap)

    override suspend fun get(key: Any): FetchData? = mutableFetchData.value[key]

    override suspend fun set(
        key: Any,
        bookmark: Bookmark,
        dateTime: DateTime
    ) {
        mutableFetchData.emit(mutableFetchData.value + (key to FetchData(dateTime, bookmark)))
    }
}
