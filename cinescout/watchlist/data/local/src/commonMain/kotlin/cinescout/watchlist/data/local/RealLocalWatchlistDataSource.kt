package cinescout.watchlist.data.local

import app.cash.paging.PagingSource
import app.cash.sqldelight.Transacter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.paging3.QueryPagingSource
import cinescout.database.MovieQueries
import cinescout.database.ScreenplayQueries
import cinescout.database.TvShowQueries
import cinescout.database.WatchlistQueries
import cinescout.database.util.suspendTransaction
import cinescout.screenplay.data.local.mapper.DatabaseScreenplayMapper
import cinescout.screenplay.data.local.mapper.toDatabaseId
import cinescout.screenplay.data.local.mapper.toDomainId
import cinescout.screenplay.data.local.mapper.toStringDatabaseId
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TvShow
import cinescout.utils.kotlin.DatabaseWriteDispatcher
import cinescout.utils.kotlin.IoDispatcher
import cinescout.watchlist.data.datasource.LocalWatchlistDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
internal class RealLocalWatchlistDataSource(
    private val mapper: DatabaseScreenplayMapper,
    private val movieQueries: MovieQueries,
    @IoDispatcher private val readDispatcher: CoroutineDispatcher,
    private val screenplayQueries: ScreenplayQueries,
    private val transacter: Transacter,
    private val tvShowQueries: TvShowQueries,
    private val watchlistQueries: WatchlistQueries,
    @DatabaseWriteDispatcher private val writeDispatcher: CoroutineDispatcher
) : LocalWatchlistDataSource {

    override suspend fun delete(id: TmdbScreenplayId) {
        watchlistQueries.suspendTransaction(writeDispatcher) {
            when (id) {
                is TmdbScreenplayId.Movie -> deleteMovieById(id.toStringDatabaseId())
                is TmdbScreenplayId.TvShow -> deleteTvShowById(id.toStringDatabaseId())
            }
        }
    }

    override suspend fun deleteAllWatchlistIds() {
        watchlistQueries.deleteAll()
    }

    override fun findPagedWatchlist(type: ScreenplayType): PagingSource<Int, Screenplay> {
        val countQuery = when (type) {
            ScreenplayType.All -> watchlistQueries.countAll()
            ScreenplayType.Movies -> watchlistQueries.countAllMovies()
            ScreenplayType.TvShows -> watchlistQueries.countAllTvShows()
        }
        fun source(limit: Long, offset: Long) = when (type) {
            ScreenplayType.All -> screenplayQueries.findAllWatchlistPaged(limit, offset, mapper::toScreenplay)
            ScreenplayType.Movies -> screenplayQueries.findAllWatchlistMoviesPaged(limit, offset, mapper::toScreenplay)
            ScreenplayType.TvShows -> screenplayQueries.findAllWatchlistTvShowsPaged(
                limit,
                offset,
                mapper::toScreenplay
            )
        }
        return QueryPagingSource(
            countQuery = countQuery,
            transacter = watchlistQueries,
            context = readDispatcher,
            queryProvider = ::source
        )
    }

    override fun findWatchlistIds(type: ScreenplayType): Flow<List<TmdbScreenplayId>> = when (type) {
        ScreenplayType.All -> watchlistQueries.findAll()
        ScreenplayType.Movies -> watchlistQueries.findAllMovies()
        ScreenplayType.TvShows -> watchlistQueries.findAllTvShows()
    }.asFlow()
        .mapToList(readDispatcher)
        .map { list -> list.map { it.toDomainId() } }

    override suspend fun insert(id: TmdbScreenplayId) {
        watchlistQueries.suspendTransaction(writeDispatcher) {
            insertWatchlist(id.toDatabaseId())
        }
    }

    override suspend fun insertAllWatchlist(screenplays: List<Screenplay>) {
        transacter.suspendTransaction(writeDispatcher) {
            for (screenplay in screenplays) {
                when (screenplay) {
                    is Movie -> movieQueries.insertMovieObject(mapper.toDatabaseMovie(screenplay))
                    is TvShow -> tvShowQueries.insertTvShowObject(mapper.toDatabaseTvShow(screenplay))
                }
                watchlistQueries.insertWatchlist(screenplay.tmdbId.toDatabaseId())
            }
        }
    }

    override suspend fun updateAllWatchlistIds(ids: List<TmdbScreenplayId>) {
        watchlistQueries.suspendTransaction(writeDispatcher) {
            deleteAll()
            for (id in ids) {
                insertWatchlist(id.toDatabaseId())
            }
        }
    }
}
