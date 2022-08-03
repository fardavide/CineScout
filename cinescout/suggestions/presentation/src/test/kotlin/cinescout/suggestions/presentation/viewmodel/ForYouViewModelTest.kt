package cinescout.suggestions.presentation.viewmodel

import app.cash.turbine.test
import arrow.core.left
import arrow.core.nonEmptyListOf
import arrow.core.right
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.design.testdata.MessageTextResTestData
import cinescout.error.NetworkError
import cinescout.movies.domain.model.SuggestionError
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.suggestions.domain.usecase.GetSuggestedMovies
import cinescout.suggestions.presentation.model.ForYouState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ForYouViewModelTest {

    private val networkErrorMapper = object : NetworkErrorToMessageMapper() {
        override fun toMessage(networkError: NetworkError) = MessageTextResTestData.NoNetworkError
    }
    private val getSuggestedMovies: GetSuggestedMovies = mockk()
    private val viewModel by lazy {
        ForYouViewModel(
            getSuggestedMovies = getSuggestedMovies,
            networkErrorMapper = networkErrorMapper
        )
    }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun `initial state is loading`() = runTest {
        // given
        val expected = ForYouState.Loading

        // when
        viewModel.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `when suggestions are loaded, state contains the movies`() = runTest {
        // given
        val movies = nonEmptyListOf(
            MovieTestData.Inception,
            MovieTestData.TheWolfOfWallStreet
        )
        val expected = ForYouState(suggestedMovies = ForYouState.SuggestedMovies.Data(movies))
        every { getSuggestedMovies() } returns flowOf(movies.right())

        // when
        viewModel.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `when no suggestion available, state contains the error message`() = runTest {
        // given
        val movies = nonEmptyListOf(
            MovieTestData.Inception,
            MovieTestData.TheWolfOfWallStreet
        )
        val expected = ForYouState(suggestedMovies = ForYouState.SuggestedMovies.NoSuggestions)
        every { getSuggestedMovies() } returns flowOf(SuggestionError.NoSuggestions.left())

        // when
        viewModel.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `when error while loading suggestions, state contains the error message`() = runTest {
        // given
        val expected = ForYouState(
            suggestedMovies = ForYouState.SuggestedMovies.Error(MessageTextResTestData.NoNetworkError)
        )
        every { getSuggestedMovies() } returns flowOf(SuggestionError.Source(NetworkError.NoNetwork).left())

        // when
        viewModel.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }
}
