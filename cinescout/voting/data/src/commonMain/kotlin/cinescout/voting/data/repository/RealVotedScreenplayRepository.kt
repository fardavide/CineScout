package cinescout.voting.data.repository

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.paging3.QueryPagingSource
import arrow.core.Option
import cinescout.database.ScreenplayFindDislikedQueries
import cinescout.database.ScreenplayFindLikedQueries
import cinescout.database.VotingQueries
import cinescout.database.util.suspendTransaction
import cinescout.lists.data.local.mapper.DatabaseListSortingMapper
import cinescout.lists.domain.ListSorting
import cinescout.screenplay.data.local.mapper.DatabaseScreenplayMapper
import cinescout.screenplay.data.local.mapper.toDatabaseId
import cinescout.screenplay.data.local.mapper.toTmdbDatabaseId
import cinescout.screenplay.data.local.mapper.toTraktDatabaseId
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.id.GenreSlug
import cinescout.screenplay.domain.model.id.ScreenplayIds
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
        genreFilter: Option<GenreSlug>,
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ): PagingSource<Int, Screenplay> {
        val databaseGenreId = genreFilter.map(GenreSlug::toDatabaseId).getOrNull()
        val sort = listSortingMapper.toDatabaseQuery(sorting)
        val countQuery = when (type) {
            ScreenplayTypeFilter.All -> votingQueries.countAllDislikedByGenreId(databaseGenreId)
            ScreenplayTypeFilter.Movies -> votingQueries.countAllDislikedMoviesByGenreId(databaseGenreId)
            ScreenplayTypeFilter.TvShows -> votingQueries.countAllDislikedTvShowsByGenreId(databaseGenreId)
        }
        fun source(limit: Long, offset: Long) = when (type) {
            ScreenplayTypeFilter.All -> findDislikedQueries.allPaged(
                genreSlug = databaseGenreId,
                sort = sort,
                limit = limit,
                offset = offset,
                mapper = mapper::toScreenplay
            )
            ScreenplayTypeFilter.Movies -> findDislikedQueries.allMoviesPaged(
                genreSlug = databaseGenreId,
                sort = sort,
                limit = limit,
                offset = offset,
                mapper = mapper::toScreenplay
            )
            ScreenplayTypeFilter.TvShows -> findDislikedQueries.allTvShowsPaged(
                genreSlug = databaseGenreId,
                sort = sort,
                limit = limit,
                offset = offset,
                mapper = mapper::toScreenplay
            )
        }
        return QueryPagingSource(
            countQuery = countQuery,
            transacter = votingQueries,
            context = readDispatcher,
            queryProvider = ::source
        )
    }

    override fun getPagedLiked(
        genreFilter: Option<GenreSlug>,
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ): PagingSource<Int, Screenplay> {
        val databaseGenreId = genreFilter.map(GenreSlug::toDatabaseId).getOrNull()
        val sort = listSortingMapper.toDatabaseQuery(sorting)
        val countQuery = when (type) {
            ScreenplayTypeFilter.All -> votingQueries.countAllLikedByGenreId(databaseGenreId)
            ScreenplayTypeFilter.Movies -> votingQueries.countAllLikedMoviesByGenreId(databaseGenreId)
            ScreenplayTypeFilter.TvShows -> votingQueries.countAllLikedTvShowsByGenreId(databaseGenreId)
        }
        fun source(limit: Long, offset: Long) = when (type) {
            ScreenplayTypeFilter.All -> findLikedQueries.allPaged(
                genreSlug = databaseGenreId,
                sort = sort,
                limit = limit,
                offset = offset,
                mapper = mapper::toScreenplay
            )

            ScreenplayTypeFilter.Movies -> findLikedQueries.allMoviesPaged(
                genreSlug = databaseGenreId,
                sort = sort,
                limit = limit,
                offset = offset,
                mapper = mapper::toScreenplay
            )

            ScreenplayTypeFilter.TvShows -> findLikedQueries.allTvShowsPaged(
                genreSlug = databaseGenreId,
                sort = sort,
                limit = limit,
                offset = offset,
                mapper = mapper::toScreenplay
            )
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
