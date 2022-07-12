package cinescout.suggestions.domain.usecase

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.SuggestionError
import cinescout.movies.domain.testdata.DiscoverMoviesParamsTestData
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class GetSuggestedMoviesTest {

    private val buildDiscoverMoviesParams: BuildDiscoverMoviesParams = mockk()
    private val movieRepository: MovieRepository = mockk()
    private val getSuggestedMovies = GetSuggestedMovies(
        buildDiscoverMoviesParams = buildDiscoverMoviesParams,
        movieRepository = movieRepository
    )

    @Test
    fun `when no suggestions`() = runTest {
        // given
        val expected = SuggestionError.NoSuggestions.left()
        every { buildDiscoverMoviesParams() } returns flowOf(expected)

        // when
        getSuggestedMovies().test {

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `when discover returns empty`() = runTest {
        // given
        val expected = SuggestionError.NoSuggestions.left()
        every { buildDiscoverMoviesParams() } returns flowOf(DiscoverMoviesParamsTestData.Random.right())
        every { movieRepository.discoverMovies(any()) } returns flowOf(emptyList<Movie>().right())

        // when
        getSuggestedMovies().test {

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }
}
