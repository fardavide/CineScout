package cinescout.screenplay.data.local.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import cinescout.database.RecommendationQueries
import cinescout.database.util.suspendTransaction
import cinescout.screenplay.data.datasource.LocalScreenplayDataSource
import cinescout.screenplay.data.local.mapper.toDatabaseId
import cinescout.screenplay.data.local.mapper.toDomainId
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.utils.kotlin.DispatcherQualifier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class RealLocalScreenplayDataSource(
    @Named(DispatcherQualifier.Io) private val readDispatcher: CoroutineDispatcher,
    private val recommendationQueries: RecommendationQueries,
    @Named(DispatcherQualifier.DatabaseWrite) private val writeDispatcher: CoroutineDispatcher
) : LocalScreenplayDataSource {

    override fun findRecommended(): Flow<List<Screenplay>> {
        TODO("Not yet implemented")
    }

    override fun findRecommendedIds(): Flow<List<TmdbScreenplayId>> = recommendationQueries.findAll()
        .asFlow()
        .mapToList(readDispatcher)
        .map { list -> list.map { it.toDomainId() } }

    override suspend fun insertRecommended(screenplays: List<Screenplay>) {
        TODO("Not yet implemented")
    }

    override suspend fun insertRecommendedIds(ids: List<TmdbScreenplayId>) {
        recommendationQueries.suspendTransaction(writeDispatcher) {
            for (id in ids) {
                recommendationQueries.insert(id.toDatabaseId())
            }
        }
    }
}
