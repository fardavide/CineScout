package cinescout.voting.data.repository

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.paging3.QueryPagingSource
import cinescout.database.ScreenplayQueries
import cinescout.database.VotingQueries
import cinescout.database.util.suspendTransaction
import cinescout.screenplay.data.local.mapper.DatabaseScreenplayMapper
import cinescout.screenplay.data.local.mapper.toDatabaseId
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.utils.kotlin.DispatcherQualifier
import cinescout.voting.domain.repository.VotedScreenplayRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class RealVotedScreenplayRepository(
    private val mapper: DatabaseScreenplayMapper,
    @Named(DispatcherQualifier.Io) private val readDispatcher: CoroutineDispatcher,
    private val screenplayQueries: ScreenplayQueries,
    private val votingQueries: VotingQueries,
    @Named(DispatcherQualifier.DatabaseWrite) private val writeDispatcher: CoroutineDispatcher
) : VotedScreenplayRepository {

    override fun getAllDisliked(type: ScreenplayType): Flow<List<Screenplay>> = when (type) {
        ScreenplayType.All -> screenplayQueries.findAllDisliked(mapper::toScreenplay)
        ScreenplayType.Movies -> screenplayQueries.findAllDislikedMovies(mapper::toScreenplay)
        ScreenplayType.TvShows -> screenplayQueries.findAllDislikedTvShows(mapper::toScreenplay)
    }.asFlow().mapToList(readDispatcher)

    override fun getAllLiked(type: ScreenplayType): Flow<List<Screenplay>> = when (type) {
        ScreenplayType.All -> screenplayQueries.findAllLiked(mapper::toScreenplay)
        ScreenplayType.Movies -> screenplayQueries.findAllLikedMovies(mapper::toScreenplay)
        ScreenplayType.TvShows -> screenplayQueries.findAllLikedTvShows(mapper::toScreenplay)
    }.asFlow().mapToList(readDispatcher)

    override fun getPagedDisliked(type: ScreenplayType): PagingSource<Int, Screenplay> {
        val countQuery = when (type) {
            ScreenplayType.All -> votingQueries.countAllDisliked()
            ScreenplayType.Movies -> votingQueries.countAllDislikedMovies()
            ScreenplayType.TvShows -> votingQueries.countAllDislikedTvShows()
        }
        fun source(limit: Long, offset: Long) = when (type) {
            ScreenplayType.All -> screenplayQueries.findAllDislikedPaged(limit, offset, mapper::toScreenplay)
            ScreenplayType.Movies -> screenplayQueries.findAllDislikedMoviesPaged(limit, offset, mapper::toScreenplay)
            ScreenplayType.TvShows -> screenplayQueries.findAllDislikedTvShowsPaged(limit, offset, mapper::toScreenplay)
        }
        return QueryPagingSource(
            countQuery = countQuery,
            transacter = votingQueries,
            context = readDispatcher,
            queryProvider = ::source
        )
    }

    override fun getPagedLiked(type: ScreenplayType): PagingSource<Int, Screenplay> {
        val countQuery = when (type) {
            ScreenplayType.All -> votingQueries.countAllLiked()
            ScreenplayType.Movies -> votingQueries.countAllLikedMovies()
            ScreenplayType.TvShows -> votingQueries.countAllLikedTvShows()
        }
        fun source(limit: Long, offset: Long) = when (type) {
            ScreenplayType.All -> screenplayQueries.findAllLikedPaged(limit, offset, mapper::toScreenplay)
            ScreenplayType.Movies -> screenplayQueries.findAllLikedMoviesPaged(limit, offset, mapper::toScreenplay)
            ScreenplayType.TvShows -> screenplayQueries.findAllLikedTvShowsPaged(limit, offset, mapper::toScreenplay)
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
