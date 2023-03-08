package cinescout.movies.data.remote.trakt

import arrow.core.right
import cinescout.movies.data.remote.sample.TraktMovieRatingSample
import cinescout.movies.data.remote.trakt.mapper.TraktMovieMapper
import cinescout.movies.data.remote.trakt.sample.GetRatingsSample
import cinescout.movies.data.remote.trakt.service.TraktMovieService
import cinescout.movies.domain.sample.MovieSample
import cinescout.screenplay.domain.model.Rating
import io.kotest.core.spec.style.AnnotationSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals

internal class RealTraktMovieDataSourceAnnotationSpecTest : AnnotationSpec() {

    private val movieMapper: TraktMovieMapper = mockk {
        every { toMovieRating(any()) } returns TraktMovieRatingSample.Inception
    }
    private val service: TraktMovieService = mockk {
        coEvery { getRatedMovies() } returns listOf(GetRatingsSample.Inception).right()
        coEvery { postAddToWatchlist(any()) } returns Unit.right()
        coEvery { postRating(any(), any()) } returns Unit.right()
    }
    private val dataSource = RealTraktMovieDataSource(
        movieMapper = movieMapper,
        service = service
    )

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
        val expected = listOf(TraktMovieRatingSample.Inception).right()

        // when
        val result = dataSource.getRatedMovies()

        // then
        assertEquals(expected, result)
    }


    @Test
    fun `post watchlist does call service`() = runTest {
        // given
        val expected = Unit.right()
        val movieId = MovieSample.Inception.tmdbId

        // when
        val result = dataSource.postAddToWatchlist(movieId)

        // then
        coVerify { service.postAddToWatchlist(movieId) }
        assertEquals(expected, result)
    }

    @Test
    fun `post rating does call service`() = runTest {
        // given
        val expected = Unit.right()
        val movieId = MovieSample.Inception.tmdbId
        Rating.of(8).tap { rating ->

            // when
            val result = dataSource.postRating(movieId, rating)

            // then
            coVerify { service.postRating(movieId, rating) }
            assertEquals(expected, result)
        }
    }
}
