package cinescout.suggestions.domain.usecase

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.ReleaseYear
import cinescout.movies.domain.model.SuggestionError
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.MovieWithRatingTestData
import cinescout.movies.domain.usecase.GetAllRatedMovies
import cinescout.utils.kotlin.emptyPagedStore
import cinescout.utils.kotlin.pagedStoreOf
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BuildDiscoverMoviesParamsTest {

    private val getAllRatedMovies: GetAllRatedMovies = mockk()
    private val buildDiscoverMoviesParams = BuildDiscoverMoviesParams(getAllRatedMovies)

    @Test
    fun `when no suggestions`() = runTest {
        // given
        val expected = SuggestionError.NoSuggestions.left()
        every { getAllRatedMovies() } returns emptyPagedStore()

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
        every { getAllRatedMovies() } returns pagedStoreOf(listOf(MovieWithRatingTestData.Inception))

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
        val expected = SuggestionError.NoSuggestions.left()
        every { getAllRatedMovies() } returns pagedStoreOf(listOf(MovieWithRatingTestData.War))

        // when
        buildDiscoverMoviesParams().test {

            // the
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }
}
