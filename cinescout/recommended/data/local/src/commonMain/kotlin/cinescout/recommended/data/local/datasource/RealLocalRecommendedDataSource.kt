package cinescout.recommended.data.local.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import cinescout.database.RecommendedQueries
import cinescout.database.util.suspendTransaction
import cinescout.recommended.data.datasource.LocalRecommendedDataSource
import cinescout.screenplay.data.local.mapper.DatabaseScreenplayIdsMapper
import cinescout.screenplay.data.local.mapper.toDatabaseId
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.utils.kotlin.DatabaseWriteDispatcher
import cinescout.utils.kotlin.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class RealLocalRecommendedDataSource(
    private val anticipatedQueries: RecommendedQueries,
    @Named(IoDispatcher) private val readDispatcher: CoroutineDispatcher,
    private val screenplayIdsMapper: DatabaseScreenplayIdsMapper,
    @Named(DatabaseWriteDispatcher) private val writeDispatcher: CoroutineDispatcher
) : LocalRecommendedDataSource {

    override fun findRecommendedIds(type: ScreenplayTypeFilter): Flow<List<ScreenplayIds>> = when (type) {
        ScreenplayTypeFilter.All -> anticipatedQueries.findAll(screenplayIdsMapper::toScreenplayIds)
        ScreenplayTypeFilter.Movies -> anticipatedQueries.findAllMovies(screenplayIdsMapper::toScreenplayIds)
        ScreenplayTypeFilter.TvShows -> anticipatedQueries.findAllTvShows(screenplayIdsMapper::toScreenplayIds)
    }.asFlow().mapToList(readDispatcher)

    override suspend fun insertRecommendedIds(ids: List<ScreenplayIds>) {
        anticipatedQueries.suspendTransaction(writeDispatcher) {
            for (id in ids) {
                anticipatedQueries.insertRecommended(
                    traktId = id.trakt.toDatabaseId(),
                    tmdbId = id.tmdb.toDatabaseId()
                )
            }
        }
    }
}
