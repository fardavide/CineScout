package cinescout.movies.domain.usecase

import app.cash.turbine.test
import arrow.core.none
import arrow.core.right
import arrow.core.some
import cinescout.movies.domain.sample.MovieWithPersonalRatingSample
import cinescout.screenplay.domain.model.Rating
import cinescout.store5.test.storeFlowOf
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class GetMoviePersonalRatingTest {

    private val getAllRatedMovies: GetAllRatedMovies = mockk()
    private val getMoviePersonalRating = GetMoviePersonalRating(getAllRatedMovies)

    @Test
    fun `get correct rating for a rated movie`() = runTest {
        // given
        val movieWithPersonalRating = MovieWithPersonalRatingSample.Inception
        every { getAllRatedMovies(refresh = any()) } returns storeFlowOf(listOf(movieWithPersonalRating))

        // when
        getMoviePersonalRating(movieWithPersonalRating.movie.tmdbId).test {

            // then
            assertEquals(movieWithPersonalRating.personalRating.some().right(), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `get none for a movie that has not been rated`() = runTest {
        // given
        val movieWithPersonalRating = MovieWithPersonalRatingSample.Inception
        every { getAllRatedMovies(refresh = any()) } returns storeFlowOf(emptyList())

        // when
        getMoviePersonalRating(movieWithPersonalRating.movie.tmdbId).test {

            // then
            assertEquals(none<Rating>().right(), awaitItem())
            awaitComplete()
        }
    }
}
