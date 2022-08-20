package cinescout.movies.domain.usecase

import app.cash.turbine.test
import arrow.core.right
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.testdata.MovieKeywordsTestData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetMovieKeywordsTest {

    private val movieRepository: MovieRepository = mockk()
    private val getMovieKeywords = GetMovieKeywords(movieRepository = movieRepository)

    @Test
    fun `get credits from repository`() = runTest {
        // given
        val keywords = MovieKeywordsTestData.Inception
        every { movieRepository.getMovieKeywords(keywords.movieId, any()) } returns flowOf(keywords.right())
        val expected = keywords.right()

        // when
        getMovieKeywords(keywords.movieId).test {

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
            verify { movieRepository.getMovieKeywords(keywords.movieId, any()) }
        }
    }
}
