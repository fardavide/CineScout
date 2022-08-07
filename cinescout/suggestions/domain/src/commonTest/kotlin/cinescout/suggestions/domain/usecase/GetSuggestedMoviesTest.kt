package cinescout.suggestions.domain.usecase

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.SuggestionError
import cinescout.movies.domain.testdata.DiscoverMoviesParamsTestData
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.MovieWithRatingTestData
import cinescout.movies.domain.usecase.GetAllDislikedMovies
import cinescout.movies.domain.usecase.GetAllLikedMovies
import cinescout.movies.domain.usecase.GetAllRatedMovies
import cinescout.store.dualSourcesEmptyPagedStore
import cinescout.store.dualSourcesPagedStoreOf
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class GetSuggestedMoviesTest {

    private val buildDiscoverMoviesParams: BuildDiscoverMoviesParams = mockk {
        coEvery { this@mockk(any()) } returns DiscoverMoviesParamsTestData.Random.right()
    }
    private val getAllDislikedMovies: GetAllDislikedMovies = mockk {
        every { this@mockk() } returns flowOf(emptyList<Movie>().right())
    }
    private val getAllLikedMovies: GetAllLikedMovies = mockk {
        every { this@mockk() } returns flowOf(emptyList<Movie>().right())
    }
    private val getAllRatedMovies: GetAllRatedMovies = mockk {
        every { this@mockk() } returns dualSourcesEmptyPagedStore()
    }
    private val movieRepository: MovieRepository = mockk()
    private val getSuggestedMovies = GetSuggestedMovies(
        buildDiscoverMoviesParams = buildDiscoverMoviesParams,
        getAllDislikedMovies = getAllDislikedMovies,
        getAllLikedMovies = getAllLikedMovies,
        getAllRatedMovies = getAllRatedMovies,
        movieRepository = movieRepository
    )

    @Test
    fun `excludes all the known movies`() = runTest {
        // given
        val expected = SuggestionError.NoSuggestions.left()
        val discoveredMovies = listOf(
            MovieTestData.Inception,
            MovieTestData.TheWolfOfWallStreet,
            MovieTestData.War
        ).right()
        every { movieRepository.discoverMovies(any()) } returns flowOf(discoveredMovies)
        every { getAllDislikedMovies() } returns flowOf(listOf(MovieTestData.War).right())
        every { getAllLikedMovies() } returns flowOf(listOf(MovieTestData.TheWolfOfWallStreet).right())
        every { getAllRatedMovies() } returns dualSourcesPagedStoreOf(listOf(MovieWithRatingTestData.Inception))

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
        val discoveredMovies = listOf(
            MovieTestData.Inception,
            MovieTestData.TheWolfOfWallStreet,
            MovieTestData.War
        ).right()
        every { movieRepository.discoverMovies(any()) } returns flowOf(discoveredMovies)
        every { getAllDislikedMovies() } returns flowOf(listOf(MovieTestData.War).right())
        every { getAllLikedMovies() } returns flowOf(listOf(MovieTestData.TheWolfOfWallStreet).right())
        every { getAllRatedMovies() } returns dualSourcesPagedStoreOf(listOf(MovieWithRatingTestData.Inception))

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
        coEvery { buildDiscoverMoviesParams(any()) } returns expected

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
