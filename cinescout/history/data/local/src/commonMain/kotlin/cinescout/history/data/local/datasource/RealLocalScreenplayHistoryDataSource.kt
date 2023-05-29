package cinescout.history.data.local.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import cinescout.database.HistoryQueries
import cinescout.database.util.suspendTransaction
import cinescout.history.data.datasource.LocalScreenplayHistoryDataSource
import cinescout.history.data.local.mapper.DatabaseScreenplayHistoryMapper
import cinescout.history.domain.model.ScreenplayHistory
import cinescout.screenplay.data.local.mapper.toStringDatabaseId
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.utils.kotlin.DatabaseWriteDispatcher
import cinescout.utils.kotlin.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class RealLocalScreenplayHistoryDataSource(
    private val historyMapper: DatabaseScreenplayHistoryMapper,
    private val historyQueries: HistoryQueries,
    @Named(IoDispatcher) private val readDispatcher: CoroutineDispatcher,
    @Named(DatabaseWriteDispatcher) private val writeDispatcher: CoroutineDispatcher
) : LocalScreenplayHistoryDataSource {

    override suspend fun deleteAll(screenplayId: ScreenplayIds) {
        historyQueries.suspendTransaction(writeDispatcher) {
            when (screenplayId) {
                is ScreenplayIds.Movie -> deleteAllByMovieTraktId(screenplayId.trakt.toStringDatabaseId())
                is ScreenplayIds.TvShow -> deleteAllByTvShowTraktId(screenplayId.trakt.toStringDatabaseId())
            }
        }
    }

    override fun find(screenplayIds: ScreenplayIds): Flow<ScreenplayHistory?> = when (screenplayIds) {
        is ScreenplayIds.Movie -> historyQueries.findAllByMovieTraktId(screenplayIds.trakt.toStringDatabaseId())
        is ScreenplayIds.TvShow -> historyQueries.findAllByTvShowTraktId(screenplayIds.trakt.toStringDatabaseId())
    }.asFlow()
        .mapToList(readDispatcher)
        .map { historyMapper.toDomainModel(screenplayIds, it) }

    override suspend fun insertAll(history: ScreenplayHistory) {
        historyQueries.suspendTransaction(writeDispatcher) {
            val databaseModels = historyMapper.toDatabaseModels(history)
            for (databaseModel in databaseModels) {
                insert(databaseModel)
            }
        }
    }
}
