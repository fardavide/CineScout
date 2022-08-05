package cinescout.suggestions.presentation.viewmodel

import app.cash.turbine.test
import arrow.core.left
import arrow.core.nonEmptyListOf
import arrow.core.right
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.design.testdata.MessageTextResTestData
import cinescout.error.NetworkError
import cinescout.movies.domain.model.SuggestionError
import cinescout.movies.domain.testdata.MovieCreditsTestData
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.usecase.GetMovieCredits
import cinescout.suggestions.domain.usecase.GetSuggestedMovies
import cinescout.suggestions.presentation.mapper.ForYouMovieUiModelMapper
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.previewdata.ForYouMovieUiModelPreviewData
import io.mockk.coEvery
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

    private val forYouMovieUiModelMapper: ForYouMovieUiModelMapper = mockk()
    private val getMovieCredits: GetMovieCredits = mockk()
    private val getSuggestedMovies: GetSuggestedMovies = mockk()
    private val networkErrorMapper = object : NetworkErrorToMessageMapper() {
        override fun toMessage(networkError: NetworkError) = MessageTextResTestData.NoNetworkError
    }
    private val viewModel by lazy {
        ForYouViewModel(
            forYouMovieUiModelMapper = forYouMovieUiModelMapper,
            getMovieCredits = getMovieCredits,
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
        val movie = ForYouMovieUiModelPreviewData.Inception
        val movies = nonEmptyListOf(
            MovieTestData.Inception,
            MovieTestData.TheWolfOfWallStreet
        )
        val expected = ForYouState(suggestedMovie = ForYouState.SuggestedMovie.Data(movie))
        every { forYouMovieUiModelMapper.toUiModel(any(), any()) } returns ForYouMovieUiModelPreviewData.Inception
        coEvery { getMovieCredits(any()) } returns flowOf(MovieCreditsTestData.Inception.right())
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
        val expected = ForYouState(suggestedMovie = ForYouState.SuggestedMovie.NoSuggestions)
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
            suggestedMovie = ForYouState.SuggestedMovie.Error(MessageTextResTestData.NoNetworkError)
        )
        every { getSuggestedMovies() } returns flowOf(SuggestionError.Source(NetworkError.NoNetwork).left())

        // when
        viewModel.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }
}
