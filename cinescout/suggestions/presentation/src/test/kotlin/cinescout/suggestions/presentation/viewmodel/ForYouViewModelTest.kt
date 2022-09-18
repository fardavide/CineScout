package cinescout.suggestions.presentation.viewmodel

import app.cash.turbine.ReceiveTurbine
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
import cinescout.settings.domain.usecase.ShouldShowForYouHint
import cinescout.suggestions.domain.usecase.GetSuggestedMovies
import cinescout.suggestions.domain.usecase.GetSuggestedMoviesWithExtras
import cinescout.suggestions.domain.usecase.IsLoggedIn
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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
    private val getSuggestedMovies: GetSuggestedMovies = mockk {
        val movies = nonEmptyListOf(
            MovieTestData.Inception,
            MovieTestData.TheWolfOfWallStreet,
            MovieTestData.War
        )
        every { this@mockk() } returns flowOf(movies.right())
    }
    private val getSuggestedMoviesWithExtras: GetSuggestedMoviesWithExtras = mockk {
        val movies = nonEmptyListOf(
            MovieWithExtrasTestData.Inception,
            MovieWithExtrasTestData.TheWolfOfWallStreet,
            MovieWithExtrasTestData.War
        )
        every { this@mockk(movieExtraRefresh = any()) } returns flowOf(movies.right())
    }
    private val isLoggedIn: IsLoggedIn = mockk {
        every { this@mockk() } returns flowOf(true)
    }
    private val networkErrorMapper = object : NetworkErrorToMessageMapper() {
        override fun toMessage(networkError: NetworkError) = MessageTextResTestData.NoNetworkError
    }
    private val shouldShowForYouHint: ShouldShowForYouHint = mockk {
        every { this@mockk() } returns flowOf(false)
    }
    private val viewModel by lazy {
        ForYouViewModel(
            addMovieToDislikedList = addMovieToDislikedList,
            addMovieToLikedList = addMovieToLikedList,
            addMovieToWatchlist = addMovieToWatchlist,
            forYouMovieUiModelMapper = forYouMovieUiModelMapper,
            getSuggestedMoviesWithExtras = getSuggestedMoviesWithExtras,
            isLoggedIn = isLoggedIn,
            networkErrorMapper = networkErrorMapper,
            shouldShowForYouHint = shouldShowForYouHint,
            suggestionsStackSize = 2
        )
    }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
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
        val expected = ForYouState(
            loggedIn = ForYouState.LoggedIn.True,
            shouldShowHint = false,
            suggestedMovie = ForYouState.SuggestedMovie.Data(movie)
        )

        // when
        viewModel.state.test {
            awaitLoading()

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `when no suggestion available, state contains the error message`() = runTest(dispatchTimeoutMs = TestTimeout) {
        // given
        val expected = ForYouState(
            loggedIn = ForYouState.LoggedIn.True,
            shouldShowHint = false,
            suggestedMovie = ForYouState.SuggestedMovie.NoSuggestions
        )
        every { getSuggestedMoviesWithExtras() } returns flowOf(SuggestionError.NoSuggestions.left())

        // when
        viewModel.state.test {
            awaitLoading()

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `when error while loading suggestions, state contains the error message`() = runTest {
        // given
        val expected = ForYouState(
            loggedIn = ForYouState.LoggedIn.True,
            shouldShowHint = false,
            suggestedMovie = ForYouState.SuggestedMovie.Error(MessageTextResTestData.NoNetworkError)
        )
        every { getSuggestedMoviesWithExtras() } returns flowOf(SuggestionError.Source(NetworkError.NoNetwork).left())

        // when
        viewModel.state.test {
            awaitLoading()

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `given suggestions are consumed, when new suggestions, then state contains the suggestions`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val expected1 = ForYouState(
            loggedIn = ForYouState.LoggedIn.True,
            shouldShowHint = false,
            suggestedMovie = ForYouState.SuggestedMovie.Data(ForYouMovieUiModelPreviewData.Inception)
        )
        val expected2 = expected1.copy(suggestedMovie = ForYouState.SuggestedMovie.Loading)
        val expected3 = expected2.copy(
            suggestedMovie = ForYouState.SuggestedMovie.Data(ForYouMovieUiModelPreviewData.TheWolfOfWallStreet)
        )
        val suggestedMovieFlow = MutableStateFlow(MovieWithExtrasTestData.Inception)
        every { getSuggestedMoviesWithExtras() } returns
            suggestedMovieFlow.map { nonEmptyListOf(it).right() }

        // when
        viewModel.state.test {
            awaitLoading()

            assertEquals(expected1, awaitItem())
            viewModel.submit(ForYouAction.Like(MovieTestData.Inception.tmdbId))

            assertEquals(expected2, awaitItem())
            suggestedMovieFlow.emit(MovieWithExtrasTestData.TheWolfOfWallStreet)

            // then
            assertEquals(expected3, awaitItem())
        }
    }

    @Test
    fun `dislike calls the use case with the correct movie id`() = runTest {
        // given
        val movieId = MovieTestData.Inception.tmdbId

        // when
        viewModel.submit(ForYouAction.Dislike(movieId))
        advanceUntilIdle()

        // then
        coVerify { addMovieToDislikedList(movieId) }
    }

    @Test
    fun `like calls the use case with the correct movie id`() = runTest {
        // given
        val movieId = MovieTestData.Inception.tmdbId

        // when
        viewModel.submit(ForYouAction.Like(movieId))
        advanceUntilIdle()

        // then
        coVerify { addMovieToLikedList(movieId) }
    }

    @Test
    fun `add to watchlist calls the use case with the correct movie id`() = runTest {
        // given
        val movieId = MovieTestData.Inception.tmdbId

        // when
        viewModel.submit(ForYouAction.AddToWatchlist(movieId))
        advanceUntilIdle()

        // then
        coVerify { addMovieToWatchlist(movieId) }
    }

    @Test
    fun `suggested movie is changed after dislike`() = runTest(dispatchTimeoutMs = TestTimeout) {
        // given
        val firstState = ForYouState(
            loggedIn = ForYouState.LoggedIn.True,
            shouldShowHint = false,
            suggestedMovie = ForYouState.SuggestedMovie.Data(ForYouMovieUiModelPreviewData.Inception)
        )
        val secondState = ForYouState(
            loggedIn = ForYouState.LoggedIn.True,
            shouldShowHint = false,
            suggestedMovie = ForYouState.SuggestedMovie.Data(ForYouMovieUiModelPreviewData.TheWolfOfWallStreet)
        )
        val movieId = MovieTestData.Inception.tmdbId

        // when
        viewModel.state.test {
            awaitLoading()

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
            loggedIn = ForYouState.LoggedIn.True,
            shouldShowHint = false,
            suggestedMovie = ForYouState.SuggestedMovie.Data(ForYouMovieUiModelPreviewData.Inception)
        )
        val secondState = ForYouState(
            loggedIn = ForYouState.LoggedIn.True,
            shouldShowHint = false,
            suggestedMovie = ForYouState.SuggestedMovie.Data(ForYouMovieUiModelPreviewData.TheWolfOfWallStreet)
        )
        val movieId = MovieTestData.Inception.tmdbId

        // when
        viewModel.state.test {
            awaitLoading()

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
            loggedIn = ForYouState.LoggedIn.True,
            shouldShowHint = false,
            suggestedMovie = ForYouState.SuggestedMovie.Data(ForYouMovieUiModelPreviewData.Inception)
        )
        val secondState = ForYouState(
            loggedIn = ForYouState.LoggedIn.True,
            shouldShowHint = false,
            suggestedMovie = ForYouState.SuggestedMovie.Data(ForYouMovieUiModelPreviewData.TheWolfOfWallStreet)
        )
        val movieId = MovieTestData.Inception.tmdbId

        // when
        viewModel.state.test {
            awaitLoading()

            assertEquals(firstState, awaitItem())
            viewModel.submit(ForYouAction.AddToWatchlist(movieId))

            // then
            assertEquals(secondState, awaitItem())
        }
    }

    private suspend fun ReceiveTurbine<ForYouState>.awaitLoading() {
        assertEquals(awaitItem(), ForYouState.Loading)
    }
}
