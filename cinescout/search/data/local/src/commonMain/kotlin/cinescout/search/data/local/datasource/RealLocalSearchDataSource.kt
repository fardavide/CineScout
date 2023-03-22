package cinescout.search.data.local.datasource

import androidx.paging.PagingSource
import app.cash.sqldelight.Transacter
import app.cash.sqldelight.paging3.QueryPagingSource
import cinescout.database.MovieQueries
import cinescout.database.ScreenplayQueries
import cinescout.database.TvShowQueries
import cinescout.database.util.suspendTransaction
import cinescout.screenplay.data.local.mapper.DatabaseScreenplayMapper
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.TvShow
import cinescout.search.data.datasource.LocalSearchDataSource
import cinescout.utils.kotlin.DispatcherQualifier
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class RealLocalSearchDataSource(
    private val mapper: DatabaseScreenplayMapper,
    private val movieQueries: MovieQueries,
    @Named(DispatcherQualifier.Io) private val readDispatcher: CoroutineDispatcher,
    private val screenplayQueries: ScreenplayQueries,
    private val transacter: Transacter,
    private val tvShowQueries: TvShowQueries,
    @Named(DispatcherQualifier.DatabaseWrite) private val writeDispatcher: CoroutineDispatcher
) : LocalSearchDataSource {

    override suspend fun insertAll(screenplays: List<Screenplay>) {
        transacter.suspendTransaction(writeDispatcher) {
            for (screenplay in screenplays) {
                when (screenplay) {
                    is Movie -> movieQueries.insertMovieObject(mapper.toDatabaseMovie(screenplay))
                    is TvShow -> tvShowQueries.insertTvShowObject(mapper.toDatabaseTvShow(screenplay))
                }
            }
        }
    }

    override fun searchPaged(type: ScreenplayType, query: String): PagingSource<Int, Screenplay> {
        val countQuery = when (type) {
            ScreenplayType.All -> screenplayQueries.countAllByQuery(query)
            ScreenplayType.Movies -> screenplayQueries.countAllMoviesByQuery(query)
            ScreenplayType.TvShows -> screenplayQueries.countAllTvShowsByQuery(query)
        }

        fun source(limit: Long, offset: Long) = when (type) {
            ScreenplayType.All ->
                screenplayQueries.findAllByQueryPaged(query, limit, offset, mapper::toScreenplay)
            ScreenplayType.Movies ->
                screenplayQueries.findAllMoviesByQueryPaged(query, limit, offset, mapper::toScreenplay)
            ScreenplayType.TvShows ->
                screenplayQueries.findAllTvShowsByQueryPaged(query, limit, offset, mapper::toScreenplay)
        }
        return QueryPagingSource(
            countQuery = countQuery,
            transacter = screenplayQueries,
            context = readDispatcher,
            queryProvider = ::source
        )
    }
}