package cinescout.voting.data.repository

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.paging3.QueryPagingSource
import cinescout.database.ScreenplayFindDislikedQueries
import cinescout.database.ScreenplayFindLikedQueries
import cinescout.database.VotingQueries
import cinescout.database.util.suspendTransaction
import cinescout.lists.data.local.mapper.DatabaseListSortingMapper
import cinescout.lists.domain.ListSorting
import cinescout.screenplay.data.local.mapper.DatabaseScreenplayMapper
import cinescout.screenplay.data.local.mapper.toTmdbDatabaseId
import cinescout.screenplay.data.local.mapper.toTraktDatabaseId
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.utils.kotlin.DatabaseWriteDispatcher
import cinescout.utils.kotlin.IoDispatcher
import cinescout.voting.domain.repository.VotedScreenplayRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class RealVotedScreenplayRepository(
    private val findDislikedQueries: ScreenplayFindDislikedQueries,
    private val findLikedQueries: ScreenplayFindLikedQueries,
    private val listSortingMapper: DatabaseListSortingMapper,
    private val mapper: DatabaseScreenplayMapper,
    @Named(IoDispatcher) private val readDispatcher: CoroutineDispatcher,
    private val votingQueries: VotingQueries,
    @Named(DatabaseWriteDispatcher) private val writeDispatcher: CoroutineDispatcher
) : VotedScreenplayRepository {

    override fun getAllDisliked(type: ScreenplayTypeFilter): Flow<List<Screenplay>> = when (type) {
        ScreenplayTypeFilter.All -> findDislikedQueries.all(mapper::toScreenplay)
        ScreenplayTypeFilter.Movies -> findDislikedQueries.allMovies(mapper::toScreenplay)
        ScreenplayTypeFilter.TvShows -> findDislikedQueries.allTvShows(mapper::toScreenplay)
    }.asFlow().mapToList(readDispatcher)

    override fun getAllLiked(type: ScreenplayTypeFilter): Flow<List<Screenplay>> = when (type) {
        ScreenplayTypeFilter.All -> findLikedQueries.all(mapper::toScreenplay)
        ScreenplayTypeFilter.Movies -> findLikedQueries.allMovies(mapper::toScreenplay)
        ScreenplayTypeFilter.TvShows -> findLikedQueries.allTvShows(mapper::toScreenplay)
    }.asFlow().mapToList(readDispatcher)

    override fun getPagedDisliked(
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ): PagingSource<Int, Screenplay> {
        val sort = listSortingMapper.toDatabaseQuery(sorting)
        val countQuery = when (type) {
            ScreenplayTypeFilter.All -> votingQueries.countAllDisliked()
            ScreenplayTypeFilter.Movies -> votingQueries.countAllDislikedMovies()
            ScreenplayTypeFilter.TvShows -> votingQueries.countAllDislikedTvShows()
        }
        fun source(limit: Long, offset: Long) = when (type) {
            ScreenplayTypeFilter.All -> findDislikedQueries.allPaged(sort, limit, offset, mapper::toScreenplay)
            ScreenplayTypeFilter.Movies -> findDislikedQueries.allMoviesPaged(sort, limit, offset, mapper::toScreenplay)
            ScreenplayTypeFilter.TvShows ->
                findDislikedQueries.allTvShowsPaged(sort, limit, offset, mapper::toScreenplay)
        }
        return QueryPagingSource(
            countQuery = countQuery,
            transacter = votingQueries,
            context = readDispatcher,
            queryProvider = ::source
        )
    }

    override fun getPagedLiked(
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ): PagingSource<Int, Screenplay> {
        val sort = listSortingMapper.toDatabaseQuery(sorting)
        val countQuery = when (type) {
            ScreenplayTypeFilter.All -> votingQueries.countAllLiked()
            ScreenplayTypeFilter.Movies -> votingQueries.countAllLikedMovies()
            ScreenplayTypeFilter.TvShows -> votingQueries.countAllLikedTvShows()
        }
        fun source(limit: Long, offset: Long) = when (type) {
            ScreenplayTypeFilter.All -> findLikedQueries.allPaged(sort, limit, offset, mapper::toScreenplay)
            ScreenplayTypeFilter.Movies -> findLikedQueries.allMoviesPaged(sort, limit, offset, mapper::toScreenplay)
            ScreenplayTypeFilter.TvShows -> findLikedQueries.allTvShowsPaged(sort, limit, offset, mapper::toScreenplay)
        }
        return QueryPagingSource(
            countQuery = countQuery,
            transacter = votingQueries,
            context = readDispatcher,
            queryProvider = ::source
        )
    }

    override suspend fun setDisliked(screenplayIds: ScreenplayIds) {
        votingQueries.suspendTransaction(writeDispatcher) {
            insert(screenplayIds.toTraktDatabaseId(), screenplayIds.toTmdbDatabaseId(), isLiked = false)
        }
    }

    override suspend fun setLiked(screenplayIds: ScreenplayIds) {
        votingQueries.suspendTransaction(writeDispatcher) {
            insert(screenplayIds.toTraktDatabaseId(), screenplayIds.toTmdbDatabaseId(), isLiked = true)
        }
    }
}
