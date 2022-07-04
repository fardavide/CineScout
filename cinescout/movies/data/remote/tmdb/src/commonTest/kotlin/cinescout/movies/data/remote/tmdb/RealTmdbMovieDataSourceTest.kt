package cinescout.movies.data.remote.tmdb

import arrow.core.right
import cinescout.movies.data.remote.testdata.TmdbMovieTestData
import cinescout.movies.data.remote.tmdb.mapper.TmdbMovieMapper
import cinescout.movies.data.remote.tmdb.service.TmdbMovieService
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.TmdbMovieIdTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RealTmdbMovieDataSourceTest {

    private val movieMapper: TmdbMovieMapper = mockk {
        every { toMovie(any()) } returns MovieTestData.Inception
    }
    private val service: TmdbMovieService = mockk {
        coEvery { getMovie(any()) } returns TmdbMovieTestData.Inception.right()
    }
    private val dataSource = RealTmdbMovieDataSource(movieMapper = movieMapper, service = service)

    @Test
    fun `get movie calls service correctly`() = runTest {
        // given
        val movieId = TmdbMovieIdTestData.Inception

        // when
        dataSource.getMovie(movieId)

        // then
        coVerify { service.getMovie(movieId) }
    }

    @Test
    fun `get movie maps movie correctly`() = runTest {
        // given
        val movieId = TmdbMovieIdTestData.Inception
        val expected = MovieTestData.Inception.right()

        // when
        val result = dataSource.getMovie(movieId)

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
