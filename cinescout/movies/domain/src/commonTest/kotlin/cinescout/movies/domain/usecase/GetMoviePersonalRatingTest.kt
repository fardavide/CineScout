package cinescout.movies.domain.usecase

import app.cash.turbine.test
import arrow.core.none
import arrow.core.right
import arrow.core.some
import cinescout.common.model.Rating
import cinescout.movies.domain.testdata.MovieWithPersonalRatingTestData
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import store.builder.emptyPagedStore
import store.builder.pagedStoreOf
import kotlin.test.Test
import kotlin.test.assertEquals

internal class GetMoviePersonalRatingTest {

    private val getAllRatedMovies: GetAllRatedMovies = mockk {
        every { this@mockk(refresh = any()) } returns emptyPagedStore()
    }
    private val getMoviePersonalRating = GetMoviePersonalRating(getAllRatedMovies)

    @Test
    fun `get correct rating for a rated movie`() = runTest {
        // given
        val movieWithPersonalRating = MovieWithPersonalRatingTestData.Inception
        every { getAllRatedMovies(refresh = any()) } returns pagedStoreOf(movieWithPersonalRating)

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
        val movieWithPersonalRating = MovieWithPersonalRatingTestData.Inception
        every { getAllRatedMovies(refresh = any()) } returns emptyPagedStore()

        // when
        getMoviePersonalRating(movieWithPersonalRating.movie.tmdbId).test {

            // then
            assertEquals(none<Rating>().right(), awaitItem())
            awaitComplete()
        }
    }
}
