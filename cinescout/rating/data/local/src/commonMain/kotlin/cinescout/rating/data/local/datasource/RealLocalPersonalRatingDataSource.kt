package cinescout.rating.data.local.datasource

import androidx.paging.PagingSource
import app.cash.sqldelight.Transacter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.paging3.QueryPagingSource
import cinescout.database.MovieQueries
import cinescout.database.PersonalRatingQueries
import cinescout.database.ScreenplayQueries
import cinescout.database.TvShowQueries
import cinescout.database.util.suspendTransaction
import cinescout.rating.data.datasource.LocalPersonalRatingDataSource
import cinescout.rating.data.local.mapper.DatabaseRatingMapper
import cinescout.rating.domain.model.ScreenplayIdWithPersonalRating
import cinescout.rating.domain.model.ScreenplayWithPersonalRating
import cinescout.screenplay.data.local.mapper.DatabaseScreenplayMapper
import cinescout.screenplay.data.local.mapper.toDatabaseId
import cinescout.screenplay.data.local.mapper.toDomainId
import cinescout.screenplay.data.local.mapper.toStringDatabaseId
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TvShow
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.utils.kotlin.DatabaseWriteDispatcher
import cinescout.utils.kotlin.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
internal class RealLocalPersonalRatingDataSource(
    private val movieQueries: MovieQueries,
    private val personalRatingQueries: PersonalRatingQueries,
    private val ratingMapper: DatabaseRatingMapper,
    @IoDispatcher private val readDispatcher: CoroutineDispatcher,
    private val screenplayMapper: DatabaseScreenplayMapper,
    private val screenplayQueries: ScreenplayQueries,
    private val transacter: Transacter,
    private val tvShowQueries: TvShowQueries,
    @DatabaseWriteDispatcher private val writeDispatcher: CoroutineDispatcher
) : LocalPersonalRatingDataSource {

    override suspend fun delete(screenplayId: TmdbScreenplayId) {
        personalRatingQueries.suspendTransaction(writeDispatcher) {
            when (screenplayId) {
                is TmdbScreenplayId.Movie -> personalRatingQueries.deleteMovieById(screenplayId.toStringDatabaseId())
                is TmdbScreenplayId.TvShow -> personalRatingQueries.deleteTvShowById(screenplayId.toStringDatabaseId())
            }
        }
    }

    override fun findPagedRatings(type: ScreenplayType): PagingSource<Int, ScreenplayWithPersonalRating> {
        val countQuery = when (type) {
            ScreenplayType.All -> personalRatingQueries.countAll()
            ScreenplayType.Movies -> personalRatingQueries.countAllMovies()
            ScreenplayType.TvShows -> personalRatingQueries.countAllTvShows()
        }
        fun source(limit: Long, offset: Long) = when (type) {
            ScreenplayType.All ->
                screenplayQueries
                    .findAllWithPersonalRatingPaged(limit, offset, ratingMapper::toScreenplayWithPersonalRating)
            ScreenplayType.Movies ->
                screenplayQueries
                    .findAllMoviesWithPersonalRatingPaged(limit, offset, ratingMapper::toScreenplayWithPersonalRating)
            ScreenplayType.TvShows ->
                screenplayQueries
                    .findAllTvShowsWithPersonalRatingPaged(limit, offset, ratingMapper::toScreenplayWithPersonalRating)
        }
        return QueryPagingSource(
            countQuery = countQuery,
            transacter = personalRatingQueries,
            context = readDispatcher,
            queryProvider = ::source
        )
    }

    override fun findRatingIds(type: ScreenplayType): Flow<List<ScreenplayIdWithPersonalRating>> =
        when (type) {
            ScreenplayType.All -> personalRatingQueries.findAll()
            ScreenplayType.Movies -> personalRatingQueries.findAllMovies()
            ScreenplayType.TvShows -> personalRatingQueries.findAllTvShows()
        }
            .asFlow()
            .mapToList(readDispatcher)
            .map { list ->
                list.map { personaRating ->
                    ScreenplayIdWithPersonalRating(
                        personalRating = Rating.of(personaRating.rating).getOrThrow(),
                        screenplayId = personaRating.tmdbId.toDomainId()
                    )
                }
            }

    override suspend fun insert(screenplayId: TmdbScreenplayId, rating: Rating) {
        personalRatingQueries.suspendTransaction(writeDispatcher) {
            insert(screenplayId.toDatabaseId(), rating.intValue)
        }
    }

    override suspend fun insertRatings(ratings: List<ScreenplayWithPersonalRating>) {
        transacter.suspendTransaction(writeDispatcher) {
            for (rating in ratings) {
                personalRatingQueries.insert(rating.screenplay.tmdbId.toDatabaseId(), rating.personalRating.intValue)
                when (val screenplay = rating.screenplay) {
                    is Movie -> movieQueries.insertMovieObject(screenplayMapper.toDatabaseMovie(screenplay))
                    is TvShow -> tvShowQueries.insertTvShowObject(screenplayMapper.toDatabaseTvShow(screenplay))
                }
            }
        }
    }

    override suspend fun updateAllRatingIds(ratings: List<ScreenplayIdWithPersonalRating>) {
        personalRatingQueries.suspendTransaction(writeDispatcher) {
            deleteAll()
            for (rating in ratings) {
                insert(rating.screenplayId.toDatabaseId(), rating.personalRating.intValue)
            }
        }
    }
}
