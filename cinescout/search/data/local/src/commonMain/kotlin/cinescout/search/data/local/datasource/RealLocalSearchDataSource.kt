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
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.TvShow
import cinescout.search.data.datasource.LocalSearchDataSource
import cinescout.utils.kotlin.DatabaseWriteDispatcher
import cinescout.utils.kotlin.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class RealLocalSearchDataSource(
    private val mapper: DatabaseScreenplayMapper,
    private val movieQueries: MovieQueries,
    @Named(IoDispatcher) private val readDispatcher: CoroutineDispatcher,
    private val screenplayQueries: ScreenplayQueries,
    private val transacter: Transacter,
    private val tvShowQueries: TvShowQueries,
    @Named(DatabaseWriteDispatcher) private val writeDispatcher: CoroutineDispatcher
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

    override fun searchPaged(type: ScreenplayTypeFilter, query: String): PagingSource<Int, Screenplay> {
        val countQuery = when (type) {
            ScreenplayTypeFilter.All -> screenplayQueries.countAllByQuery(query)
            ScreenplayTypeFilter.Movies -> screenplayQueries.countAllMoviesByQuery(query)
            ScreenplayTypeFilter.TvShows -> screenplayQueries.countAllTvShowsByQuery(query)
        }

        fun source(limit: Long, offset: Long) = when (type) {
            ScreenplayTypeFilter.All ->
                screenplayQueries.findAllByQueryPaged(query, limit, offset, mapper::toScreenplay)
            ScreenplayTypeFilter.Movies ->
                screenplayQueries.findAllMoviesByQueryPaged(query, limit, offset, mapper::toScreenplay)
            ScreenplayTypeFilter.TvShows ->
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
