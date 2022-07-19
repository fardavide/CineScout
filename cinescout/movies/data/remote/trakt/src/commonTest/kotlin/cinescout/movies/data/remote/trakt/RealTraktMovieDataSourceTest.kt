package cinescout.movies.data.remote.trakt

import arrow.core.right
import cinescout.movies.data.remote.trakt.mapper.TraktMovieMapper
import cinescout.movies.data.remote.trakt.service.TraktMovieService
import cinescout.movies.data.remote.trakt.testdata.GetRatingsTestData
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.testdata.MovieRatingTestData
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.store.pagedDataOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RealTraktMovieDataSourceTest {

    private val movieMapper: TraktMovieMapper = mockk {
        every { toMovieRating(any()) } returns MovieRatingTestData.Inception
    }
    private val service: TraktMovieService = mockk {
        coEvery { getRatedMovies() } returns pagedDataOf(GetRatingsTestData.Inception).right()
    }
    private val dataSource = RealTraktMovieDataSource(movieMapper = movieMapper, service = service)

    @Test
    fun `get rated movies calls service correctly`() = runTest {
        // when
        dataSource.getRatedMovies()

        // then
        coVerify { service.getRatedMovies() }
    }

    @Test
    fun `get rated movies maps correctly`() = runTest {
        // given
        val expected = pagedDataOf(MovieRatingTestData.Inception).right()

        // when
        val result = dataSource.getRatedMovies()

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
