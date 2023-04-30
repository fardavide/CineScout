package cinescout.popular.data.local.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import cinescout.database.PopularQueries
import cinescout.database.util.suspendTransaction
import cinescout.popular.data.datasource.LocalPopularDataSource
import cinescout.screenplay.data.local.mapper.DatabaseScreenplayIdsMapper
import cinescout.screenplay.data.local.mapper.toDatabaseId
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.utils.kotlin.DatabaseWriteDispatcher
import cinescout.utils.kotlin.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class RealLocalPopularDataSource(
    private val anticipatedQueries: PopularQueries,
    @Named(IoDispatcher) private val readDispatcher: CoroutineDispatcher,
    private val screenplayIdsMapper: DatabaseScreenplayIdsMapper,
    @Named(DatabaseWriteDispatcher) private val writeDispatcher: CoroutineDispatcher
) : LocalPopularDataSource {

    override fun findPopularIds(type: ScreenplayTypeFilter): Flow<List<ScreenplayIds>> = when (type) {
        ScreenplayTypeFilter.All -> anticipatedQueries.findAll(screenplayIdsMapper::toScreenplayIds)
        ScreenplayTypeFilter.Movies -> anticipatedQueries.findAllMovies(screenplayIdsMapper::toScreenplayIds)
        ScreenplayTypeFilter.TvShows -> anticipatedQueries.findAllTvShows(screenplayIdsMapper::toScreenplayIds)
    }.asFlow().mapToList(readDispatcher)

    override suspend fun insertPopularIds(ids: List<ScreenplayIds>) {
        anticipatedQueries.suspendTransaction(writeDispatcher) {
            for (id in ids) {
                anticipatedQueries.insertPopular(
                    traktId = id.trakt.toDatabaseId(),
                    tmdbId = id.tmdb.toDatabaseId()
                )
            }
        }
    }
}
