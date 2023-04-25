package cinescout.anticipated.data.local.datasource

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import cinescout.anticipated.data.datasource.LocalAnticipatedDataSource
import cinescout.database.AnticipatedQueries
import cinescout.database.MovieQueries
import cinescout.database.ScreenplayFindAnticipatedQueries
import cinescout.database.TvShowQueries
import cinescout.database.util.suspendTransaction
import cinescout.screenplay.data.local.mapper.DatabaseScreenplayIdsMapper
import cinescout.screenplay.data.local.mapper.DatabaseScreenplayMapper
import cinescout.screenplay.data.local.mapper.toDatabaseId
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.TvShow
import cinescout.utils.kotlin.DatabaseWriteDispatcher
import cinescout.utils.kotlin.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class RealLocalAnticipatedDataSource(
    private val anticipatedQueries: AnticipatedQueries,
    private val findAnticipatedQueries: ScreenplayFindAnticipatedQueries,
    private val movieQueries: MovieQueries,
    @Named(IoDispatcher) private val readDispatcher: CoroutineDispatcher,
    private val screenplayIdsMapper: DatabaseScreenplayIdsMapper,
    private val screenplayMapper: DatabaseScreenplayMapper,
    private val transacter: Transacter,
    private val tvShowQueries: TvShowQueries,
    @Named(DatabaseWriteDispatcher) private val writeDispatcher: CoroutineDispatcher
) : LocalAnticipatedDataSource {

    override fun findMostAnticipated(type: ScreenplayType): Flow<List<Screenplay>> = when (type) {
        ScreenplayType.All -> findAnticipatedQueries.all(screenplayMapper::toScreenplay)
        ScreenplayType.Movies -> findAnticipatedQueries.allMovies(screenplayMapper::toScreenplay)
        ScreenplayType.TvShows -> findAnticipatedQueries.allTvShows(screenplayMapper::toScreenplay)
    }.asFlow().mapToList(readDispatcher)

    override fun findMostAnticipatedIds(type: ScreenplayType): Flow<List<ScreenplayIds>> = when (type) {
        ScreenplayType.All -> anticipatedQueries.findAll(screenplayIdsMapper::toScreenplayIds)
        ScreenplayType.Movies -> anticipatedQueries.findAllMovies(screenplayIdsMapper::toScreenplayIds)
        ScreenplayType.TvShows -> anticipatedQueries.findAllTvShows(screenplayIdsMapper::toScreenplayIds)
    }.asFlow().mapToList(readDispatcher)

    override suspend fun insertMostAnticipated(screenplays: List<Screenplay>) {
        transacter.suspendTransaction(writeDispatcher) {
            for (screenplay in screenplays) {
                when (screenplay) {
                    is Movie -> movieQueries.insertMovieObject(screenplayMapper.toDatabaseMovie(screenplay))
                    is TvShow -> tvShowQueries.insertTvShowObject(screenplayMapper.toDatabaseTvShow(screenplay))
                }
                anticipatedQueries.insertAnticipated(
                    traktId = screenplay.traktId.toDatabaseId(),
                    tmdbId = screenplay.tmdbId.toDatabaseId()
                )
            }
        }
    }

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
