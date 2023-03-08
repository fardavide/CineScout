package cinescout.movies.data.remote.trakt.service

import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.movies.data.remote.trakt.sample.GetRatingsSample
import cinescout.movies.data.remote.trakt.testutil.MockTraktMovieEngine
import cinescout.movies.domain.sample.MovieSample
import cinescout.network.trakt.CineScoutTraktClient
import cinescout.network.trakt.TraktAuthProvider
import cinescout.screenplay.domain.model.Rating
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.core.test.TestCase
import io.ktor.client.HttpClient
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals

class RealTraktMovieServiceTest : AnnotationSpec() {

    private val authProvider: TraktAuthProvider = mockk {
        coEvery { refreshToken() } returns ""
    }
    private lateinit var client: HttpClient
    private lateinit var service: TraktMovieService

    override suspend fun beforeAny(testCase: TestCase) {
        client = CineScoutTraktClient(engine = MockTraktMovieEngine(), authProvider = authProvider)
        service = RealTraktMovieService(client)
    }

    @Test
    fun `get rated movies returns error if not authenticated`() = runTest {
        // given
        val expected = NetworkError.Unauthorized.left()
        coEvery { authProvider.accessToken() } returns null

        // when
        val result = service.getRatedMovies()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `get rated movies returns result if authenticated`() = runTest {
        // given
        val expected = listOf(GetRatingsSample.Inception).right()
        coEvery { authProvider.accessToken() } returns "token"

        // when
        val result = service.getRatedMovies()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `post add to watchlist returns error if not authenticated`() = runTest {
        // given
        val movieId = MovieSample.Inception.tmdbId
        val expected = NetworkError.Unauthorized.left()
        coEvery { authProvider.accessToken() } returns null

        // when
        val result = service.postAddToWatchlist(movieId)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `post add to watchlist returns result if authenticated`() = runTest {
        // given
        val movieId = MovieSample.Inception.tmdbId
        val expected = Unit.right()
        coEvery { authProvider.accessToken() } returns "token"

        // when
        val result = service.postAddToWatchlist(movieId)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `post rating returns error if not authenticated`() = runTest {
        // given
        val movieId = MovieSample.Inception.tmdbId
        val expected = NetworkError.Unauthorized.left()
        coEvery { authProvider.accessToken() } returns null
        Rating.of(8).tap { rating ->

            // when
            val result = service.postRating(movieId, rating)

            // then
            assertEquals(expected, result)
        }
    }

    @Test
    fun `post rating returns result if authenticated`() = runTest {
        // given
        val movieId = MovieSample.Inception.tmdbId
        val expected = Unit.right()
        coEvery { authProvider.accessToken() } returns "token"
        Rating.of(8).tap { rating ->

            // when
            val result = service.postRating(movieId, rating)

            // then
            assertEquals(expected, result)
        }
    }
}