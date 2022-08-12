package cinescout.suggestions.domain.usecase

import app.cash.turbine.test
import arrow.core.left
import arrow.core.nonEmptyListOf
import arrow.core.right
import cinescout.error.DataError
import cinescout.error.NetworkError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.SuggestionError
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.suggestions.domain.model.SuggestionsMode
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class GetSuggestedMoviesTest {

    private val movieRepository: MovieRepository = mockk()
    private val updateSuggestedMovies: UpdateSuggestedMovies = mockk {
        coEvery { invoke(suggestionsMode = any()) } returns Unit.right()
    }
    private val getSuggestedMovies = GetSuggestedMovies(
        movieRepository = movieRepository,
        updateSuggestedMovies = updateSuggestedMovies,
        updateIfSuggestionsLessThan = TestMinimumSuggestions
    )

    @Test
    fun `gets suggestions from repository`() = runTest {
        // given
        val movies = nonEmptyListOf(
            MovieTestData.Inception,
            MovieTestData.TheWolfOfWallStreet,
            MovieTestData.War
        ).right()
        every { movieRepository.getSuggestedMovies() } returns flowOf(movies)

        // when
        getSuggestedMovies().test {

            // then
            assertEquals(movies, awaitItem())
            awaitComplete()
            verify { movieRepository.getSuggestedMovies() }
        }
    }

    @Test
    fun `updates suggestions if less than the declared threshold`() = runTest {
        // given
        val movies = nonEmptyListOf(
            MovieTestData.Inception,
            MovieTestData.TheWolfOfWallStreet
        ).right()
        every { movieRepository.getSuggestedMovies() } returns flowOf(movies)

        // when
        getSuggestedMovies().test {
            awaitItem()
            awaitComplete()

            // then
            coVerify { updateSuggestedMovies(SuggestionsMode.Quick) }
        }
    }

    @Test
    fun `does not updates suggestions if same or more than the declared threshold`() = runTest {
        // given
        val movies = nonEmptyListOf(
            MovieTestData.Inception,
            MovieTestData.TheWolfOfWallStreet,
            MovieTestData.War
        ).right()
        every { movieRepository.getSuggestedMovies() } returns flowOf(movies)

        // when
        getSuggestedMovies().test {
            awaitItem()
            awaitComplete()

            // then
            coVerify { updateSuggestedMovies wasNot Called }
        }
    }

    @Test
    @Ignore("Waiting for actors in KMP: https://github.com/Kotlin/kotlinx.coroutines/issues/87")
    fun `does not start update while already updating`() = runTest {
        // given
        val moviesFlow = flow {
            emit(nonEmptyListOf(MovieTestData.Inception).right())
            delay(100)
            emit(nonEmptyListOf(MovieTestData.TheWolfOfWallStreet).right())
        }
        every { movieRepository.getSuggestedMovies() } returns moviesFlow

        // when
        getSuggestedMovies().test {
            awaitItem()
            awaitItem()

            // then
            coVerify(exactly = 1) { updateSuggestedMovies(SuggestionsMode.Quick) }
        }
    }

    @Test
    fun `emits error from updating if there are no stored suggestions from repository`() = runTest {
        // given
        val expected = SuggestionError.Source(DataError.Remote(NetworkError.NoNetwork)).left()
        every { movieRepository.getSuggestedMovies() } returns flowOf(DataError.Local.NoCache.left())
        coEvery { updateSuggestedMovies(any()) } returns expected

        // when
        getSuggestedMovies().test {

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    companion object {

        const val TestMinimumSuggestions = 3
    }
}
