package cinescout.movies.data.local

import cinescout.database.MovieQueries
import cinescout.movies.data.local.mapper.DatabaseMovieMapper
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.TmdbMovieIdTestData
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import mapper.toDatabaseId
import kotlin.test.Test

class RealLocalMovieDataSourceTest {

    private val databaseMovieMapper: DatabaseMovieMapper = mockk()
    private val dispatcher = StandardTestDispatcher()
    private val movieQueries: MovieQueries = mockk(relaxed = true)
    private val source = RealLocalMovieDataSource(
        databaseMovieMapper = databaseMovieMapper,
        dispatcher = dispatcher,
        movieQueries = movieQueries
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
    fun `insert rating does nothing`() = runTest {
        Rating.of(8).tap { rating ->
            source.insertRating(MovieTestData.Inception, rating)
            // TODO does nothing
        }
    }

    @Test
    fun `insert watchlist does nothing`() = runTest {
        source.insertWatchlist(MovieTestData.Inception)
        // TODO does nothing
    }
}
