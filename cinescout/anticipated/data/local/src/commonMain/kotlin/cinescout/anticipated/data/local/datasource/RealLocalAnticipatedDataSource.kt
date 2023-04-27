package cinescout.anticipated.data.local.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import cinescout.anticipated.data.datasource.LocalAnticipatedDataSource
import cinescout.database.AnticipatedQueries
import cinescout.database.util.suspendTransaction
import cinescout.screenplay.data.local.mapper.DatabaseScreenplayIdsMapper
import cinescout.screenplay.data.local.mapper.toDatabaseId
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.utils.kotlin.DatabaseWriteDispatcher
import cinescout.utils.kotlin.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class RealLocalAnticipatedDataSource(
    private val anticipatedQueries: AnticipatedQueries,
    @Named(IoDispatcher) private val readDispatcher: CoroutineDispatcher,
    private val screenplayIdsMapper: DatabaseScreenplayIdsMapper,
    @Named(DatabaseWriteDispatcher) private val writeDispatcher: CoroutineDispatcher
) : LocalAnticipatedDataSource {

    override fun findMostAnticipatedIds(type: ScreenplayType): Flow<List<ScreenplayIds>> = when (type) {
        ScreenplayType.All -> anticipatedQueries.findAll(screenplayIdsMapper::toScreenplayIds)
        ScreenplayType.Movies -> anticipatedQueries.findAllMovies(screenplayIdsMapper::toScreenplayIds)
        ScreenplayType.TvShows -> anticipatedQueries.findAllTvShows(screenplayIdsMapper::toScreenplayIds)
    }.asFlow().mapToList(readDispatcher)

    override suspend fun insertMostAnticipatedIds(ids: List<ScreenplayIds>) {
        anticipatedQueries.suspendTransaction(writeDispatcher) {
            for (id in ids) {
                anticipatedQueries.insertAnticipated(
                    traktId = id.trakt.toDatabaseId(),
                    tmdbId = id.tmdb.toDatabaseId()
                )
            }
        }
    }
}
