package cinescout.movies.domain.usecase

import app.cash.turbine.test
import arrow.core.right
import arrow.core.some
import cinescout.movies.domain.testdata.*
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetMovieExtrasTest {

    private val getIsMovieInWatchlist: GetIsMovieInWatchlist = mockk {
        every { this@mockk(id = any(), refresh = any()) } returns flowOf(true.right())
    }
    private val getMovieCredits: GetMovieCredits = mockk {
        every { this@mockk(id = any(), refresh = any()) } returns flowOf(MovieCreditsTestData.Inception.right())
    }
    private val getMovieDetails: GetMovieDetails = mockk {
        every { this@mockk(movieId = any(), refresh = any()) } returns flowOf(MovieWithDetailsTestData.Inception.right())
    }
    private val getMovieKeywords: GetMovieKeywords = mockk {
        every { this@mockk(movieId = any(), refresh = any()) } returns flowOf(MovieKeywordsTestData.Inception.right())
    }
    private val getMoviePersonalRating: GetMoviePersonalRating = mockk {
        every { this@mockk(movieId = any(), refresh = any()) } returns
            flowOf(MovieWithPersonalRatingTestData.Inception.personalRating.some().right())
    }
    private val getMovieExtras = GetMovieExtras(
        getIsMovieInWatchlist = getIsMovieInWatchlist,
        getMovieCredits = getMovieCredits,
        getMovieDetails = getMovieDetails,
        getMovieKeywords = getMovieKeywords,
        getMoviePersonalRating = getMoviePersonalRating
    )

    @Test
    fun `get watchlist value, credits, details, keywords and rating`() = runTest {
        // given
        val expected = MovieWithExtrasTestData.Inception.right()
        val movieId = MovieTestData.Inception.tmdbId

        // when
        getMovieExtras(movieId).test {

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `get watchlist value, credits, details, keywords and personal rating from movie with personal rating`() =
        runTest {
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
