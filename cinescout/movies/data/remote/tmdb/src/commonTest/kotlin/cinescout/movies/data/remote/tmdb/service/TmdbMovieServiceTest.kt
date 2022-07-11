package cinescout.movies.data.remote.tmdb.service

import arrow.core.right
import cinescout.movies.data.remote.testdata.TmdbMovieTestData
import cinescout.movies.data.remote.tmdb.model.PostRating
import cinescout.movies.data.remote.tmdb.testutil.MockTmdbMovieEngine
import cinescout.network.CineScoutClient
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TmdbMovieServiceTest {

    private val client = CineScoutClient(MockTmdbMovieEngine())
    private val service = TmdbMovieService(client)

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
    fun `post rating returns success`() = runTest {
        // given
        val movie = TmdbMovieTestData.Inception
        val expected = Unit.right()

        // when
        val result = service.postRating(movie.id, PostRating.Request(8))

        // then
        assertEquals(expected, result)
    }
}
