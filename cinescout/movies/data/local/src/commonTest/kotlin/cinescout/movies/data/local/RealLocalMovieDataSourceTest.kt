package cinescout.movies.data.local

import cinescout.database.MovieQueries
import cinescout.database.MovieRatingQueries
import cinescout.database.WatchlistQueries
import cinescout.movies.data.local.mapper.DatabaseMovieMapper
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.TmdbMovieIdTestData
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import cinescout.movies.data.local.mapper.toDatabaseId
import cinescout.movies.data.local.mapper.toDatabaseRating
import kotlin.test.Test

class RealLocalMovieDataSourceTest {

    private val databaseMovieMapper: DatabaseMovieMapper = mockk()
    private val dispatcher = StandardTestDispatcher()
    private val movieQueries: MovieQueries = mockk(relaxed = true)
    private val movieRatingQueries: MovieRatingQueries = mockk(relaxed = true)
    private val watchlistQueries: WatchlistQueries = mockk(relaxed = true)
    private val source = RealLocalMovieDataSource(
        databaseMovieMapper = databaseMovieMapper,
        dispatcher = dispatcher,
        movieQueries = movieQueries,
        movieRatingQueries = movieRatingQueries,
        watchlistQueries = watchlistQueries,
    )

    @Test
    fun `get movie get from queries`() = runTest {
        // given
        val movieId = TmdbMovieIdTestData.Inception

        // when
        source.findMovie(movieId)

        // then
        verify { movieQueries.findById(movieId.toDatabaseId()) }
    }

    @Test
    fun `insert call queries`() = runTest {
        // given
        val movie = MovieTestData.Inception

        // when
        source.insert(movie)

        // then
        verify { movieQueries.insertMovie(tmdbId = movie.tmdbId.toDatabaseId(), title = movie.title) }
    }

    @Test
    fun `insert rating call queries`() = runTest {
        // given
        val movie = MovieTestData.Inception
        Rating.of(8).tap { rating ->

            // when
            source.insertRating(movie, rating)

            // then
            verify {
                movieRatingQueries.insertRating(
                    tmdbId = movie.tmdbId.toDatabaseId(),
                    rating = rating.toDatabaseRating()
                )
            }
        }
    }

    @Test
    fun `insert watchlist calls queries`() = runTest {
        // given
        val movie = MovieTestData.Inception

        // when
        source.insertWatchlist(movie)

        // then
        verify { watchlistQueries.insertWatchlist(tmdbId = movie.tmdbId.toDatabaseId()) }
    }
}
