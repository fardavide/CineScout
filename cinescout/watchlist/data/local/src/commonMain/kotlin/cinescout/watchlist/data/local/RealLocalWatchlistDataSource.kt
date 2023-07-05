package cinescout.watchlist.data.local

import app.cash.paging.PagingSource
import app.cash.sqldelight.Transacter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.paging3.QueryPagingSource
import cinescout.database.MovieQueries
import cinescout.database.ScreenplayFindWatchlistQueries
import cinescout.database.ScreenplayGenreQueries
import cinescout.database.TvShowQueries
import cinescout.database.WatchlistQueries
import cinescout.database.ext.ids
import cinescout.database.util.suspendTransaction
import cinescout.lists.data.local.mapper.DatabaseListSortingMapper
import cinescout.lists.domain.ListParams
import cinescout.screenplay.data.local.mapper.DatabaseScreenplayMapper
import cinescout.screenplay.data.local.mapper.toDatabaseId
import cinescout.screenplay.data.local.mapper.toDomainIds
import cinescout.screenplay.data.local.mapper.toStringDatabaseId
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.ScreenplayWithGenreSlugs
import cinescout.screenplay.domain.model.TvShow
import cinescout.screenplay.domain.model.id.GenreSlug
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.screenplay.domain.model.id.TmdbMovieId
import cinescout.screenplay.domain.model.id.TmdbScreenplayId
import cinescout.screenplay.domain.model.id.TmdbTvShowId
import cinescout.utils.kotlin.DatabaseWriteDispatcher
import cinescout.utils.kotlin.IoDispatcher
import cinescout.watchlist.data.datasource.LocalWatchlistDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class RealLocalWatchlistDataSource(
    private val findWatchlistQueries: ScreenplayFindWatchlistQueries,
    private val listSortingMapper: DatabaseListSortingMapper,
    private val mapper: DatabaseScreenplayMapper,
    private val movieQueries: MovieQueries,
    @Named(IoDispatcher) private val readDispatcher: CoroutineDispatcher,
    private val screenplayGenreQueries: ScreenplayGenreQueries,
    private val transacter: Transacter,
    private val tvShowQueries: TvShowQueries,
    private val watchlistQueries: WatchlistQueries,
    @Named(DatabaseWriteDispatcher) private val writeDispatcher: CoroutineDispatcher
) : LocalWatchlistDataSource {

    override suspend fun delete(id: TmdbScreenplayId) {
        watchlistQueries.suspendTransaction(writeDispatcher) {
            when (id) {
                is TmdbMovieId -> deleteMovieById(id.toStringDatabaseId())
                is TmdbTvShowId -> deleteTvShowById(id.toStringDatabaseId())
            }
        }
    }

    override fun findPagedWatchlist(params: ListParams): PagingSource<Int, Screenplay> {
        val databaseGenreSlug = params.genreFilter.map(GenreSlug::toDatabaseId).getOrNull()
        val sort = listSortingMapper.toDatabaseQuery(params.sorting)
        val countQuery = when (params.type) {
            ScreenplayTypeFilter.All -> watchlistQueries.countAllByGenreId(databaseGenreSlug)
            ScreenplayTypeFilter.Movies -> watchlistQueries.countAllMoviesByGenreId(databaseGenreSlug)
            ScreenplayTypeFilter.TvShows -> watchlistQueries.countAllTvShowsByGenreId(databaseGenreSlug)
        }
        fun source(limit: Long, offset: Long) = when (params.type) {
            ScreenplayTypeFilter.All -> findWatchlistQueries.allPaged(
                genreSlug = databaseGenreSlug,
                sort = sort,
                limit = limit,
                offset = offset,
                mapper = mapper::toScreenplay
            )
            ScreenplayTypeFilter.Movies -> findWatchlistQueries.allMoviesPaged(
                genreSlug = databaseGenreSlug,
                sort = sort,
                limit = limit,
                offset = offset,
                mapper = mapper::toScreenplay
            )
            ScreenplayTypeFilter.TvShows -> findWatchlistQueries.allTvShowsPaged(
                genreSlug = databaseGenreSlug,
                sort = sort,
                limit = limit,
                offset = offset,
                mapper = mapper::toScreenplay
            )
        }
        return QueryPagingSource(
            countQuery = countQuery,
            transacter = transacter,
            context = readDispatcher,
            queryProvider = ::source
        )
    }

    override fun findWatchlistIds(type: ScreenplayTypeFilter): Flow<List<ScreenplayIds>> = when (type) {
        ScreenplayTypeFilter.All -> watchlistQueries.findAll()
        ScreenplayTypeFilter.Movies -> watchlistQueries.findAllMovies()
        ScreenplayTypeFilter.TvShows -> watchlistQueries.findAllTvShows()
    }.asFlow()
        .mapToList(readDispatcher)
        .map { list -> list.map { it.ids.toDomainIds() } }

    override suspend fun insert(ids: ScreenplayIds) {
        watchlistQueries.suspendTransaction(writeDispatcher) {
            insertWatchlist(ids.trakt.toDatabaseId(), ids.tmdb.toDatabaseId())
        }
    }

    override suspend fun insertAllWatchlist(screenplays: List<ScreenplayWithGenreSlugs>) {
        transacter.suspendTransaction(writeDispatcher) {
            for (screenplayWithGenreSlugs in screenplays) {
                val screenplay = screenplayWithGenreSlugs.screenplay
                when (screenplay) {
                    is Movie -> movieQueries.insertMovieObject(mapper.toDatabaseMovie(screenplay))
                    is TvShow -> tvShowQueries.insertTvShowObject(mapper.toDatabaseTvShow(screenplay))
                }
                watchlistQueries.insertWatchlist(screenplay.traktId.toDatabaseId(), screenplay.tmdbId.toDatabaseId())
                for (genreSlug in screenplayWithGenreSlugs.genreSlugs) {
                    screenplayGenreQueries.insert(screenplay.traktId.toDatabaseId(), genreSlug.toDatabaseId())
                }
            }
        }
    }

    override suspend fun updateAllWatchlist(
        screenplays: List<ScreenplayWithGenreSlugs>,
        type: ScreenplayTypeFilter
    ) {
        transacter.suspendTransaction(writeDispatcher) {
            when (type) {
                ScreenplayTypeFilter.All -> watchlistQueries.deleteAll()
                ScreenplayTypeFilter.Movies -> watchlistQueries.deleteAllMovies()
                ScreenplayTypeFilter.TvShows -> watchlistQueries.deleteAllTvShows()
            }
            for (screenplayWithGenreSlugs in screenplays) {
                val screenplay = screenplayWithGenreSlugs.screenplay
                when (screenplay) {
                    is Movie -> movieQueries.insertMovieObject(mapper.toDatabaseMovie(screenplay))
                    is TvShow -> tvShowQueries.insertTvShowObject(mapper.toDatabaseTvShow(screenplay))
                }
                watchlistQueries.insertWatchlist(screenplay.traktId.toDatabaseId(), screenplay.tmdbId.toDatabaseId())
                for (genreSlug in screenplayWithGenreSlugs.genreSlugs) {
                    screenplayGenreQueries.insert(screenplay.traktId.toDatabaseId(), genreSlug.toDatabaseId())
                }
            }
        }
    }

    override suspend fun updateAllWatchlistIds(ids: List<ScreenplayIds>, type: ScreenplayTypeFilter) {
        watchlistQueries.suspendTransaction(writeDispatcher) {
            when (type) {
                ScreenplayTypeFilter.All -> watchlistQueries.deleteAll()
                ScreenplayTypeFilter.Movies -> watchlistQueries.deleteAllMovies()
                ScreenplayTypeFilter.TvShows -> watchlistQueries.deleteAllTvShows()
            }
            for (id in ids) {
                insertWatchlist(id.trakt.toDatabaseId(), id.tmdb.toDatabaseId())
            }
        }
    }
}
