package cinescout.movies.domain.usecase

import app.cash.turbine.test
import arrow.core.right
import cinescout.movies.domain.testdata.MovieWithRatingTestData
import cinescout.store.Paging
import cinescout.store.dualSourcesPagedStoreOf
import cinescout.store.toPagedData
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
        val expected = allRatedMovies.map { it.movie }.toPagedData(Paging.Page.DualSources.Initial).right()
        every { getAllRatedMovies() } returns dualSourcesPagedStoreOf(allRatedMovies)

        // when
        getAllKnownMovies().test {

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }
}
