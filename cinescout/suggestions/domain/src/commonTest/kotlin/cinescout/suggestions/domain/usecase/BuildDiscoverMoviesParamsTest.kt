package cinescout.suggestions.domain.usecase

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.MovieWithRatingTestData
import cinescout.movies.domain.usecase.GetAllRatedMovies
import cinescout.suggestions.domain.model.DiscoverMoviesParams
import cinescout.suggestions.domain.model.NoSuggestions
import cinescout.suggestions.domain.model.ReleaseYear
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BuildDiscoverMoviesParamsTest {

    private val getAllRatedMovies: GetAllRatedMovies = mockk()
    private val buildDiscoverMoviesParams = BuildDiscoverMoviesParams(getAllRatedMovies)

    @Test
    fun `when no suggestions`() = runTest {
        // given
        val expected = NoSuggestions.left()
        every { getAllRatedMovies() } returns flowOf(DataError.Local.NoCache.left())

        // when
        buildDiscoverMoviesParams().test {

            // the
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `build from rated movies`() = runTest {
        // given
        val expected = DiscoverMoviesParams(ReleaseYear(MovieTestData.Inception.releaseDate.year)).right()
        every { getAllRatedMovies() } returns flowOf(listOf(MovieWithRatingTestData.Inception).right())

        // when
        buildDiscoverMoviesParams().test {

            // the
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `build from rated movies, when all rating are below the threshold`() = runTest {
        // given
        val expected = NoSuggestions.left()
        every { getAllRatedMovies() } returns flowOf(listOf(MovieWithRatingTestData.War).right())

        // when
        buildDiscoverMoviesParams().test {

            // the
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }
}
