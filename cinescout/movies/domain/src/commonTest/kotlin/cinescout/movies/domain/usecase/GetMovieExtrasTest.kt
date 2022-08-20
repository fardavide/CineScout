package cinescout.movies.domain.usecase

import app.cash.turbine.test
import arrow.core.none
import arrow.core.right
import cinescout.movies.domain.model.MovieWithExtras
import cinescout.movies.domain.testdata.MovieCreditsTestData
import cinescout.movies.domain.testdata.MovieKeywordsTestData
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.MovieWithDetailsTestData
import cinescout.movies.domain.testdata.MovieWithExtrasTestData
import cinescout.movies.domain.testdata.MovieWithPersonalRatingTestData
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetMovieExtrasTest {

    private val getMovieCredits: GetMovieCredits = mockk {
        every { this@mockk(any()) } returns flowOf(MovieCreditsTestData.Inception.right())
    }
    private val getMovieDetails: GetMovieDetails = mockk {
        every { this@mockk(any(), any()) } returns flowOf(MovieWithDetailsTestData.Inception.right())
    }
    private val getMovieKeywords: GetMovieKeywords = mockk {
        every { this@mockk(any()) } returns flowOf(MovieKeywordsTestData.Inception.right())
    }
    private val getMovieExtras = GetMovieExtras(
        getMovieCredits = getMovieCredits,
        getMovieDetails = getMovieDetails,
        getMovieKeywords = getMovieKeywords
    )

    @Test
    fun `get credits, details and keywords`() = runTest {
        // given
        val expected = MovieWithExtras(
            credits = MovieCreditsTestData.Inception,
            movieWithDetails = MovieWithDetailsTestData.Inception,
            keywords = MovieKeywordsTestData.Inception,
            personalRating = none() // TODO fetch rating
        ).right()
        val movieId = MovieTestData.Inception.tmdbId

        // when
        getMovieExtras(movieId).test {

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `get credits, details and keywords from movie with personal rating`() = runTest {
        // given
        val expected = MovieWithExtrasTestData.Inception.right()
        val movieWithPersonalRating = MovieWithPersonalRatingTestData.Inception

        // when
        getMovieExtras(movieWithPersonalRating).test {

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }
}
