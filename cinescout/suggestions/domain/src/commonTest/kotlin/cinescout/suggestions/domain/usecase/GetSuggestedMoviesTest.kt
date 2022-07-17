package cinescout.suggestions.domain.usecase

import app.cash.turbine.test
import arrow.core.left
import arrow.core.nonEmptyListOf
import arrow.core.right
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.SuggestionError
import cinescout.movies.domain.testdata.DiscoverMoviesParamsTestData
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.usecase.GetAllKnownMovies
import cinescout.store.emptyPagedStore
import cinescout.store.pagedStoreOf
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class GetSuggestedMoviesTest {

    private val buildDiscoverMoviesParams: BuildDiscoverMoviesParams = mockk {
        every { this@mockk() } returns flowOf(DiscoverMoviesParamsTestData.Random.right())
    }
    private val getAllKnownMovies: GetAllKnownMovies = mockk {
        every { this@mockk() } returns emptyPagedStore()
    }
    private val movieRepository: MovieRepository = mockk()
    private val getSuggestedMovies = GetSuggestedMovies(
        buildDiscoverMoviesParams = buildDiscoverMoviesParams,
        getAllKnownMovies = getAllKnownMovies,
        movieRepository = movieRepository
    )

    @Test
    fun `excludes all the known movies`() = runTest {
        // given
        val expected = nonEmptyListOf(MovieTestData.TheWolfOfWallStreet).right()
        val discoveredMovies = listOf(MovieTestData.Inception, MovieTestData.TheWolfOfWallStreet).right()
        val allKnownMovies = listOf(MovieTestData.Inception)
        every { movieRepository.discoverMovies(any()) } returns flowOf(discoveredMovies)
        every { getAllKnownMovies() } returns pagedStoreOf(allKnownMovies)

        // when
        getSuggestedMovies().test {

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `when all suggestions are known movies`() = runTest {
        // given
        val expected = SuggestionError.NoSuggestions.left()
        val discoveredMovies = listOf(MovieTestData.Inception).right()
        val allKnownMovies = listOf(MovieTestData.Inception)
        every { movieRepository.discoverMovies(any()) } returns flowOf(discoveredMovies)
        every { getAllKnownMovies() } returns pagedStoreOf(allKnownMovies)

        // when
        getSuggestedMovies().test {

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

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
        every { movieRepository.discoverMovies(any()) } returns flowOf(emptyList<Movie>().right())

        // when
        getSuggestedMovies().test {

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }
}
