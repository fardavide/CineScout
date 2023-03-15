package cinescout.screenplay.data.local.datasource

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import cinescout.database.MovieQueries
import cinescout.database.RecommendationQueries
import cinescout.database.ScreenplayQueries
import cinescout.database.SimilarQueries
import cinescout.database.TvShowQueries
import cinescout.database.util.suspendTransaction
import cinescout.screenplay.data.datasource.LocalScreenplayDataSource
import cinescout.screenplay.data.local.mapper.DatabaseScreenplayMapper
import cinescout.screenplay.data.local.mapper.toDatabaseId
import cinescout.screenplay.data.local.mapper.toDomainId
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayGenres
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TvShow
import cinescout.utils.kotlin.DispatcherQualifier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class RealLocalScreenplayDataSource(
    private val databaseScreenplayMapper: DatabaseScreenplayMapper,
    private val movieQueries: MovieQueries,
    @Named(DispatcherQualifier.Io) private val readDispatcher: CoroutineDispatcher,
    private val recommendationQueries: RecommendationQueries,
    private val screenplayQueries: ScreenplayQueries,
    private val similarQueries: SimilarQueries,
    private val transacter: Transacter,
    private val tvShowQueries: TvShowQueries,
    @Named(DispatcherQualifier.DatabaseWrite) private val writeDispatcher: CoroutineDispatcher
) : LocalScreenplayDataSource {

    override fun findRecommended(): Flow<List<Screenplay>> =
        screenplayQueries.findAllRecommended(databaseScreenplayMapper::toScreenplay)
            .asFlow()
            .mapToList(readDispatcher)

    override fun findRecommendedIds(): Flow<List<TmdbScreenplayId>> = recommendationQueries.findAll()
        .asFlow()
        .mapToList(readDispatcher)
        .map { list -> list.map { it.toDomainId() } }

    override fun findScreenplay(id: TmdbScreenplayId): Flow<Screenplay?> =
        screenplayQueries.findById(id.value.toLong(), databaseScreenplayMapper::toScreenplay)
            .asFlow()
            .mapToOneOrNull(readDispatcher)

    override fun findScreenplayGenres(id: TmdbScreenplayId): Flow<ScreenplayGenres?> {
        TODO("Not yet implemented")
    }

    override fun findSimilar(id: TmdbScreenplayId): Flow<List<Screenplay>> =
        screenplayQueries.findSimilar(id.toDatabaseId(), databaseScreenplayMapper::toScreenplay)
            .asFlow()
            .mapToList(readDispatcher)

    override suspend fun insert(screenplay: Screenplay) {
        transacter.suspendTransaction(writeDispatcher) {
            when (screenplay) {
                is Movie -> movieQueries.insertMovieObject(databaseScreenplayMapper.toDatabaseMovie(screenplay))
                is TvShow -> tvShowQueries.insertTvShowObject(databaseScreenplayMapper.toDatabaseTvShow(screenplay))
            }
        }
    }

    override suspend fun insert(screenplays: List<Screenplay>) {
        transacter.suspendTransaction(writeDispatcher) {
            for (screenplay in screenplays) {
                when (screenplay) {
                    is Movie -> movieQueries.insertMovieObject(databaseScreenplayMapper.toDatabaseMovie(screenplay))
                    is TvShow -> tvShowQueries.insertTvShowObject(databaseScreenplayMapper.toDatabaseTvShow(screenplay))
                }
            }
        }
    }

    override suspend fun insertRecommended(screenplays: List<Screenplay>) {
        transacter.suspendTransaction(writeDispatcher) {
            for (screenplay in screenplays) {
                recommendationQueries.insert(screenplay.tmdbId.toDatabaseId())
                when (screenplay) {
                    is Movie -> movieQueries.insertMovieObject(databaseScreenplayMapper.toDatabaseMovie(screenplay))
                    is TvShow -> tvShowQueries.insertTvShowObject(databaseScreenplayMapper.toDatabaseTvShow(screenplay))
                }
            }
        }
    }

    override suspend fun insertRecommendedIds(ids: List<TmdbScreenplayId>) {
        recommendationQueries.suspendTransaction(writeDispatcher) {
            for (id in ids) {
                recommendationQueries.insert(id.toDatabaseId())
            }
        }
    }

    override suspend fun insertScreenplayGenres(screenplayGenres: ScreenplayGenres) {
        TODO("Not yet implemented")
    }

    override suspend fun insertSimilar(id: TmdbScreenplayId, screenplays: List<Screenplay>) {
        transacter.suspendTransaction(writeDispatcher) {
            for (screenplay in screenplays) {
                similarQueries.insert(id.toDatabaseId(), screenplay.tmdbId.toDatabaseId())
                when (screenplay) {
                    is Movie -> movieQueries.insertMovieObject(databaseScreenplayMapper.toDatabaseMovie(screenplay))
                    is TvShow -> tvShowQueries.insertTvShowObject(databaseScreenplayMapper.toDatabaseTvShow(screenplay))
                }
            }
        }
    }
}
