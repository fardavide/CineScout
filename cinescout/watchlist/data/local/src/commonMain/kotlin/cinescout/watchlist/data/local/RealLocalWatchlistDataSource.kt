package cinescout.watchlist.data.local

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.paging3.QueryPagingSource
import cinescout.database.ScreenplayQueries
import cinescout.database.WatchlistQueries
import cinescout.database.util.suspendTransaction
import cinescout.lists.domain.ListType
import cinescout.screenplay.data.local.mapper.toDatabaseId
import cinescout.screenplay.data.local.mapper.toDomainId
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.utils.kotlin.DispatcherQualifier
import cinescout.watchlist.data.LocalWatchlistDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class RealLocalWatchlistDataSource(
    private val mapper: DatabaseScreenplayMapper,
    @Named(DispatcherQualifier.Io) private val readDispatcher: CoroutineDispatcher,
    private val screenplayQueries: ScreenplayQueries,
    private val watchlistQueries: WatchlistQueries,
    @Named(DispatcherQualifier.DatabaseWrite) private val writeDispatcher: CoroutineDispatcher
) : LocalWatchlistDataSource {

    override fun findPagedWatchlist(type: ListType): PagingSource<Int, Screenplay> {
        val countQuery = when (type) {
            ListType.All -> watchlistQueries.countAll()
            ListType.Movies -> watchlistQueries.countAllMovies()
            ListType.TvShows -> watchlistQueries.countAllTvShows()
        }
        fun source(limit: Long, offset: Long) = when (type) {
            ListType.All -> screenplayQueries.findAllWatchlistPaged(limit, offset, mapper::toScreenplay)
            ListType.Movies -> screenplayQueries.findAllWatchlistMoviesPaged(limit, offset, mapper::toScreenplay)
            ListType.TvShows -> screenplayQueries.findAllWatchlistTvShowsPaged(limit, offset, mapper::toScreenplay)
        }
        return QueryPagingSource(
            countQuery = countQuery,
            transacter = watchlistQueries,
            context = readDispatcher,
            queryProvider = ::source
        )
    }

    override fun findWatchlistIds(type: ListType): Flow<List<TmdbScreenplayId>> = when (type) {
        ListType.All -> watchlistQueries.findAll()
        ListType.Movies -> watchlistQueries.findAllMovies()
        ListType.TvShows -> watchlistQueries.findAllTvShows()
    }.asFlow()
        .mapToList(readDispatcher)
        .map { list -> list.map { it.toDomainId() } }

    override suspend fun insertAllWatchlist(screenplays: List<Screenplay>) {
        TODO("Not yet implemented")
    }

    override suspend fun insertAllWatchlistIds(ids: List<TmdbScreenplayId>) {
        watchlistQueries.suspendTransaction(writeDispatcher) {
            for (id in ids) {
                watchlistQueries.insertWatchlist(id.toDatabaseId())
            }
        }
    }

    override suspend fun deleteAllWatchlistIds() {
        watchlistQueries.deleteAll()
    }
}
