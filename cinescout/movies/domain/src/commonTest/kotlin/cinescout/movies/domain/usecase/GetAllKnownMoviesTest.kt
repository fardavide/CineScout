package cinescout.movies.domain.usecase

import app.cash.turbine.test
import arrow.core.right
import cinescout.model.Paging
import cinescout.model.toPagedData
import cinescout.movies.domain.testdata.MovieWithRatingTestData
import cinescout.utils.kotlin.pagedStoreOf
import io.mockk.every
import io.mockk.mockk
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
        val expected = allRatedMovies.map { it.movie }.toPagedData(Paging.Page(1, 1)).right()
        every { getAllRatedMovies() } returns pagedStoreOf(allRatedMovies)

        // when
        getAllKnownMovies().test {

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }
}
