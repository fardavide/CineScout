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
import cinescout.movies.domain.testdata.MovieWithExtrasTestData
import cinescout.movies.domain.usecase.AddMovieToDislikedList
import cinescout.movies.domain.usecase.AddMovieToLikedList
import cinescout.movies.domain.usecase.AddMovieToWatchlist
import cinescout.movies.domain.usecase.GetMovieExtras
import cinescout.suggestions.domain.usecase.GetSuggestedMovies
import cinescout.suggestions.presentation.mapper.ForYouMovieUiModelMapper
import cinescout.suggestions.presentation.model.ForYouAction
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.previewdata.ForYouMovieUiModelPreviewData
import cinescout.test.kotlin.TestTimeout
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ForYouViewModelTest {

    private val addMovieToDislikedList: AddMovieToDislikedList = mockk(relaxUnitFun = true)
    private val addMovieToLikedList: AddMovieToLikedList = mockk(relaxUnitFun = true)
    private val addMovieToWatchlist: AddMovieToWatchlist = mockk {
        coEvery { this@mockk(any()) } returns Unit.right()
    }
    private val forYouMovieUiModelMapper: ForYouMovieUiModelMapper = mockk {
        every { toUiModel(MovieWithExtrasTestData.Inception) } returns ForYouMovieUiModelPreviewData.Inception
        every { toUiModel(MovieWithExtrasTestData.TheWolfOfWallStreet) } returns
            ForYouMovieUiModelPreviewData.TheWolfOfWallStreet
        every { toUiModel(MovieWithExtrasTestData.War) } returns ForYouMovieUiModelPreviewData.War
    }
    private val getMovieExtras: GetMovieExtras = mockk {
        coEvery { this@mockk(MovieTestData.Inception) } returns flowOf(MovieWithExtrasTestData.Inception.right())
        coEvery { this@mockk(MovieTestData.TheWolfOfWallStreet) } returns
            flowOf(MovieWithExtrasTestData.TheWolfOfWallStreet.right())
        coEvery { this@mockk(MovieTestData.War) } returns flowOf(MovieWithExtrasTestData.War.right())
    }
    private val getSuggestedMovies: GetSuggestedMovies = mockk {
        val movies = nonEmptyListOf(
            MovieTestData.Inception,
            MovieTestData.TheWolfOfWallStreet,
            MovieTestData.War
        )
        every { this@mockk() } returns flowOf(movies.right())
    }
    private val networkErrorMapper = object : NetworkErrorToMessageMapper() {
        override fun toMessage(networkError: NetworkError) = MessageTextResTestData.NoNetworkError
    }
    private val viewModel by lazy {
        ForYouViewModel(
            addMovieToDislikedList = addMovieToDislikedList,
            addMovieToLikedList = addMovieToLikedList,
            addMovieToWatchlist = addMovieToWatchlist,
            forYouMovieUiModelMapper = forYouMovieUiModelMapper,
            getMovieExtras = getMovieExtras,
            getSuggestedMovies = getSuggestedMovies,
            networkErrorMapper = networkErrorMapper,
            suggestionsStackSize = 2
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
        every { getSuggestedMovies() } returns emptyFlow()

        // when
        viewModel.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `when suggestions are loaded, state contains a movie`() = runTest {
        // given
        val movie = ForYouMovieUiModelPreviewData.Inception
        val expected = ForYouState(suggestedMovie = ForYouState.SuggestedMovie.Data(movie))

        // when
        viewModel.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `when no suggestion available, state contains the error message`() = runTest {
        // given
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

    @Test
    fun `dislike calls the use case with the correct movie id`() = runTest {
        // given
        val movieId = MovieTestData.Inception.tmdbId

        // when
        viewModel.submit(ForYouAction.Dislike(movieId))

        // then
        coVerify { addMovieToDislikedList(movieId) }
    }

    @Test
    fun `like calls the use case with the correct movie id`() = runTest {
        // given
        val movieId = MovieTestData.Inception.tmdbId

        // when
        viewModel.submit(ForYouAction.Like(movieId))

        // then
        coVerify { addMovieToLikedList(movieId) }
    }

    @Test
    fun `add to watchlist calls the use case with the correct movie id`() = runTest {
        // given
        val movieId = MovieTestData.Inception.tmdbId

        // when
        viewModel.submit(ForYouAction.AddToWatchlist(movieId))

        // then
        coVerify { addMovieToWatchlist(movieId) }
    }

    @Test
    fun `suggested movie is changed after dislike`() = runTest(dispatchTimeoutMs = TestTimeout) {
        // given
        val firstState = ForYouState(
            suggestedMovie = ForYouState.SuggestedMovie.Data(ForYouMovieUiModelPreviewData.Inception)
        )
        val secondState = ForYouState(
            suggestedMovie = ForYouState.SuggestedMovie.Data(ForYouMovieUiModelPreviewData.TheWolfOfWallStreet)
        )
        val movieId = MovieTestData.Inception.tmdbId

        // when
        viewModel.state.test {

            assertEquals(firstState, awaitItem())
            viewModel.submit(ForYouAction.Dislike(movieId))

            // then
            assertEquals(secondState, awaitItem())
        }
    }

    @Test
    fun `suggested movie is changed after like`() = runTest(dispatchTimeoutMs = TestTimeout) {
        // given
        val firstState = ForYouState(
            suggestedMovie = ForYouState.SuggestedMovie.Data(ForYouMovieUiModelPreviewData.Inception)
        )
        val secondState = ForYouState(
            suggestedMovie = ForYouState.SuggestedMovie.Data(ForYouMovieUiModelPreviewData.TheWolfOfWallStreet)
        )
        val movieId = MovieTestData.Inception.tmdbId

        // when
        viewModel.state.test {

            assertEquals(firstState, awaitItem())
            viewModel.submit(ForYouAction.Like(movieId))

            // then
            assertEquals(secondState, awaitItem())
        }
    }

    @Test
    fun `suggested movie is changed after add to watchlist`() = runTest(dispatchTimeoutMs = TestTimeout) {
        // given
        val firstState = ForYouState(
            suggestedMovie = ForYouState.SuggestedMovie.Data(ForYouMovieUiModelPreviewData.Inception)
        )
        val secondState = ForYouState(
            suggestedMovie = ForYouState.SuggestedMovie.Data(ForYouMovieUiModelPreviewData.TheWolfOfWallStreet)
        )
        val movieId = MovieTestData.Inception.tmdbId

        // when
        viewModel.state.test {

            assertEquals(firstState, awaitItem())
            viewModel.submit(ForYouAction.AddToWatchlist(movieId))

            // then
            assertEquals(secondState, awaitItem())
        }
    }
}
