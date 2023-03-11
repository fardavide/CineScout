package cinescout.movies.domain.usecase

import app.cash.turbine.test
import arrow.core.right
import cinescout.movies.domain.sample.MovieWithPersonalRatingSample
import cinescout.movies.domain.store.FakeRatedMoviesStore
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAllRatedMoviesTest {

    private val ratedMoviesStore = FakeRatedMoviesStore(
        ratedMovies = listOf(
            MovieWithPersonalRatingSample.Inception,
            MovieWithPersonalRatingSample.TheWolfOfWallStreet
        )
    )
    private val getAllRatedMovies = RealGetAllRatedMovies(ratedMoviesStore = ratedMoviesStore)

    @Test
    fun `get all rated movies from repository`() = runTest {
        // given
        val moviesWithRating = listOf(
            MovieWithPersonalRatingSample.Inception,
            MovieWithPersonalRatingSample.TheWolfOfWallStreet
        )
        val expected = moviesWithRating.right()

        // when
        getAllRatedMovies().test {

            // then
            assertEquals(expected, awaitItem().dataOrNull())
            awaitComplete()
        }
    }
}
