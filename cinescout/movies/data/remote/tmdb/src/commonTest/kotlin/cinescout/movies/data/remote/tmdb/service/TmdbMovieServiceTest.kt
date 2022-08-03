package cinescout.movies.data.remote.tmdb.service

import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.movies.data.remote.testdata.TmdbMovieTestData
import cinescout.movies.data.remote.tmdb.model.PostRating
import cinescout.movies.data.remote.tmdb.testdata.DiscoverMoviesResponseTestData
import cinescout.movies.data.remote.tmdb.testdata.GetRatedMoviesResponseTestData
import cinescout.movies.data.remote.tmdb.testutil.MockTmdbMovieEngine
import cinescout.movies.domain.testdata.DiscoverMoviesParamsTestData
import cinescout.network.CineScoutClient
import cinescout.network.tmdb.TmdbAuthProvider
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TmdbMovieServiceTest {

    private val authProvider: TmdbAuthProvider = mockk()
    private val client = CineScoutClient(MockTmdbMovieEngine())
    private val service = TmdbMovieService(authProvider = authProvider, client = client)

    @Test
    fun `discover movies returns right movies`() = runTest {
        // given
        val expected = DiscoverMoviesResponseTestData.TwoMovies.right()

        // when
        val result = service.discoverMovies(DiscoverMoviesParamsTestData.Random)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `get movie returns right movie`() = runTest {
        // given
        val movie = TmdbMovieTestData.Inception
        val expected = movie.right()

        // when
        val result = service.getMovie(movie.id)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `get rated movies returns error if not authenticated`() = runTest {
        // given
        val expected = NetworkError.Unauthorized.left()
        every { authProvider.accountId() } returns null

        // when
        val result = service.getRatedMovies(1)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `get rated movies returns result if authenticated`() = runTest {
        // given
        val expected = GetRatedMoviesResponseTestData.OneMovie.right()
        every { authProvider.accountId() } returns "123"

        // when
        val result = service.getRatedMovies(1)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `post rating returns success`() = runTest {
        // given
        val movie = TmdbMovieTestData.Inception
        val expected = Unit.right()

        // when
        val result = service.postRating(movie.id, PostRating.Request(8.0))

        // then
        assertEquals(expected, result)
    }
}
