package cinescout.movies.data.remote.tmdb

import arrow.core.right
import cinescout.movies.data.remote.testdata.TmdbMovieTestData
import cinescout.movies.data.remote.tmdb.testutil.MockTmdbMovieEngine
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.network.CineScoutClient
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RealTmdbMovieDataSourceTest {

    private val client = CineScoutClient(MockTmdbMovieEngine())
    private val dataSource = RealTmdbMovieDataSource(client)

    @Test
    fun `get movie returns right movie`() = runTest {
        // given
        val movie = TmdbMovieTestData.Inception
        val expected = movie.right()

        // when
        val result = dataSource.getMovie(movie.id)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `post watchlist does nothing`() = runTest {
        // given
        val movie = MovieTestData.Inception

        // when
        dataSource.postWatchlist(movie)

        // then TODO
    }

    @Test
    fun `post rating does nothing`() = runTest {
        // given
        val movie = MovieTestData.Inception
        Rating.of(8).tap { rating ->

            // when
            dataSource.postRating(movie, rating)

            // then TODO
        }
    }
}
