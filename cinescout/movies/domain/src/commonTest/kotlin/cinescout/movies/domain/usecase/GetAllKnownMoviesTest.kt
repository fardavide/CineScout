package cinescout.movies.domain.usecase

import app.cash.turbine.test
import arrow.core.right
import cinescout.movies.domain.testdata.MovieWithRatingTestData
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAllKnownMoviesTest {

    private val getAllRatedMovies: GetAllRatedMovies = mockk()
    private val getAllKnownMovies = GetAllKnownMovies(getAllRatedMovies = getAllRatedMovies)

    @Test
    fun `includes all the rated movies`() = runTest {
        // given
        val allRatedMovies = listOf(
            MovieWithRatingTestData.Inception,
            MovieWithRatingTestData.War
        )
        val expected = allRatedMovies.map { it.movie }.right()
        every { getAllRatedMovies() } returns flowOf(allRatedMovies.right())

        // when
        getAllKnownMovies().test {

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }
}
