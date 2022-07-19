package cinescout.movies.data.remote.trakt.service

import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.movies.data.remote.trakt.testdata.GetRatingsTestData
import cinescout.movies.data.remote.trakt.testutil.MockTraktMovieEngine
import cinescout.network.trakt.CineScoutTraktClient
import cinescout.network.trakt.TraktAuthProvider
import cinescout.store.pagedDataOf
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TraktMovieServiceTest {

    private val authProvider: TraktAuthProvider = mockk()
    private val client = CineScoutTraktClient(engine = MockTraktMovieEngine(), authProvider = authProvider)
    private val service = TraktMovieService(client)

    @Test
    fun `get rated movies returns error if not authenticated`() = runTest {
        // given
        val expected = NetworkError.Unauthorized.left()
        every { authProvider.accessToken() } returns null

        // when
        val result = service.getRatedMovies()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `get rated movies returns result if authenticated`() = runTest {
        // given
        val expected = pagedDataOf(GetRatingsTestData.Inception).right()
        every { authProvider.accessToken() } returns "token"

        // when
        val result = service.getRatedMovies()

        // then
        assertEquals(expected, result)
    }
}
