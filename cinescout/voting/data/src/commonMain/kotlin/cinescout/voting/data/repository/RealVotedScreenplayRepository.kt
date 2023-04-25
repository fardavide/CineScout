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
import cinescout.screenplay.data.local.mapper.toDatabaseId
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.TmdbScreenplayId
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

    override fun getAllDisliked(type: ScreenplayType): Flow<List<Screenplay>> = when (type) {
        ScreenplayType.All -> findDislikedQueries.all(mapper::toScreenplay)
        ScreenplayType.Movies -> findDislikedQueries.allMovies(mapper::toScreenplay)
        ScreenplayType.TvShows -> findDislikedQueries.allTvShows(mapper::toScreenplay)
    }.asFlow().mapToList(readDispatcher)

    override fun getAllLiked(type: ScreenplayType): Flow<List<Screenplay>> = when (type) {
        ScreenplayType.All -> findLikedQueries.all(mapper::toScreenplay)
        ScreenplayType.Movies -> findLikedQueries.allMovies(mapper::toScreenplay)
        ScreenplayType.TvShows -> findLikedQueries.allTvShows(mapper::toScreenplay)
    }.asFlow().mapToList(readDispatcher)

    override fun getPagedDisliked(sorting: ListSorting, type: ScreenplayType): PagingSource<Int, Screenplay> {
        val sort = listSortingMapper.toDatabaseQuery(sorting)
        val countQuery = when (type) {
            ScreenplayType.All -> votingQueries.countAllDisliked()
            ScreenplayType.Movies -> votingQueries.countAllDislikedMovies()
            ScreenplayType.TvShows -> votingQueries.countAllDislikedTvShows()
        }
        fun source(limit: Long, offset: Long) = when (type) {
            ScreenplayType.All -> findDislikedQueries.allPaged(sort, limit, offset, mapper::toScreenplay)
            ScreenplayType.Movies -> findDislikedQueries.allMoviesPaged(sort, limit, offset, mapper::toScreenplay)
            ScreenplayType.TvShows -> findDislikedQueries.allTvShowsPaged(sort, limit, offset, mapper::toScreenplay)
        }
        return QueryPagingSource(
            countQuery = countQuery,
            transacter = votingQueries,
            context = readDispatcher,
            queryProvider = ::source
        )
    }

    override fun getPagedLiked(sorting: ListSorting, type: ScreenplayType): PagingSource<Int, Screenplay> {
        val sort = listSortingMapper.toDatabaseQuery(sorting)
        val countQuery = when (type) {
            ScreenplayType.All -> votingQueries.countAllLiked()
            ScreenplayType.Movies -> votingQueries.countAllLikedMovies()
            ScreenplayType.TvShows -> votingQueries.countAllLikedTvShows()
        }
        fun source(limit: Long, offset: Long) = when (type) {
            ScreenplayType.All -> findLikedQueries.allPaged(sort, limit, offset, mapper::toScreenplay)
            ScreenplayType.Movies -> findLikedQueries.allMoviesPaged(sort, limit, offset, mapper::toScreenplay)
            ScreenplayType.TvShows -> findLikedQueries.allTvShowsPaged(sort, limit, offset, mapper::toScreenplay)
        }
        return QueryPagingSource(
            countQuery = countQuery,
            transacter = votingQueries,
            context = readDispatcher,
            queryProvider = ::source
        )
    }

    override suspend fun setDisliked(screenplayId: TmdbScreenplayId) {
        votingQueries.suspendTransaction(writeDispatcher) {
            insert(screenplayId.toDatabaseId(), isLiked = false)
        }
    }

    override suspend fun setLiked(screenplayId: TmdbScreenplayId) {
        votingQueries.suspendTransaction(writeDispatcher) {
            insert(screenplayId.toDatabaseId(), isLiked = true)
        }
    }
}
