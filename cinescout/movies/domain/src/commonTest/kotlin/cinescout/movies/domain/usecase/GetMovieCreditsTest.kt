package cinescout.movies.domain.usecase

import app.cash.turbine.test
import arrow.core.right
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.testdata.MovieCreditsTestData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import store.builder.storeOf
import kotlin.test.Test
import kotlin.test.assertEquals

class GetMovieCreditsTest {

    private val movieRepository: MovieRepository = mockk()
    private val getMovieCredits = GetMovieCredits(movieRepository = movieRepository)

    @Test
    fun `get credits from repository`() = runTest {
        // given
        val credits = MovieCreditsTestData.Inception
        every { movieRepository.getMovieCredits(credits.movieId, any()) } returns storeOf(credits)
        val expected = credits.right()

        // when
        getMovieCredits(credits.movieId).test {

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
            verify { movieRepository.getMovieCredits(credits.movieId, any()) }
        }
    }
}
