package cinescout.suggestions.presentation.viewmodel

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import arrow.core.left
import arrow.core.nonEmptyListOf
import arrow.core.right
import cinescout.common.model.SuggestionError
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.design.testdata.MessageTextResTestData
import cinescout.error.NetworkError
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.MovieWithExtrasTestData
import cinescout.movies.domain.usecase.AddMovieToDislikedList
import cinescout.movies.domain.usecase.AddMovieToLikedList
import cinescout.movies.domain.usecase.AddMovieToWatchlist
import cinescout.settings.domain.usecase.ShouldShowForYouHint
import cinescout.suggestions.domain.usecase.GetSuggestedMoviesWithExtras
import cinescout.suggestions.domain.usecase.GetSuggestedTvShowsWithExtras
import cinescout.suggestions.presentation.mapper.ForYouItemUiModelMapper
import cinescout.suggestions.presentation.model.ForYouAction
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.sample.ForYouMovieUiModelSample
import cinescout.suggestions.presentation.sample.ForYouTvShowUiModelSample
import cinescout.test.kotlin.TestTimeout
import cinescout.tvshows.domain.testdata.TmdbTvShowIdTestData
import cinescout.tvshows.domain.testdata.TvShowWithExtrasTestData
import cinescout.tvshows.domain.usecase.AddTvShowToDislikedList
import cinescout.tvshows.domain.usecase.AddTvShowToLikedList
import cinescout.tvshows.domain.usecase.AddTvShowToWatchlist
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val addTvShowToDislikedList: AddTvShowToDislikedList = mockk(relaxUnitFun = true)
    private val addTvShowToLikedList: AddTvShowToLikedList = mockk(relaxUnitFun = true)
    private val addTvShowToWatchlist: AddTvShowToWatchlist = mockk {
        coEvery { this@mockk(any()) } returns Unit.right()
    }
    private val forYouItemUiModelMapper: ForYouItemUiModelMapper = mockk {
        every { toUiModel(MovieWithExtrasTestData.Inception) } returns ForYouMovieUiModelSample.Inception
        every { toUiModel(MovieWithExtrasTestData.TheWolfOfWallStreet) } returns
            ForYouMovieUiModelSample.TheWolfOfWallStreet
        every { toUiModel(MovieWithExtrasTestData.War) } returns ForYouMovieUiModelSample.War
        every { toUiModel(TvShowWithExtrasTestData.BreakingBad) } returns ForYouTvShowUiModelSample.BreakingBad
        every { toUiModel(TvShowWithExtrasTestData.Dexter) } returns ForYouTvShowUiModelSample.Dexter
        every { toUiModel(TvShowWithExtrasTestData.Grimm) } returns ForYouTvShowUiModelSample.Grimm
    }
    private val getSuggestedMoviesWithExtras: GetSuggestedMoviesWithExtras = mockk {
        val movies = nonEmptyListOf(
            MovieWithExtrasTestData.Inception,
            MovieWithExtrasTestData.TheWolfOfWallStreet
        )
        every { this@mockk(movieExtraRefresh = any(), take = any()) } returns flowOf(movies.right())
    }
    private val getSuggestedTvShowsWithExtras: GetSuggestedTvShowsWithExtras = mockk {
        val tvShows = nonEmptyListOf(
            TvShowWithExtrasTestData.Dexter,
            TvShowWithExtrasTestData.Grimm
        )
        every { this@mockk(tvShowExtraRefresh = any(), take = any()) } returns flowOf(tvShows.right())
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
            addTvShowToDislikedList = addTvShowToDislikedList,
            addTvShowToLikedList = addTvShowToLikedList,
            addTvShowToWatchlist = addTvShowToWatchlist,
            forYouItemUiModelMapper = forYouItemUiModelMapper,
            getSuggestedMoviesWithExtras = getSuggestedMoviesWithExtras,
            getSuggestedTvShowsWithExtras = getSuggestedTvShowsWithExtras,
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

        // when
        viewModel.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `hint is shown when suggestions are loaded for the first time`() = runTest {
        // given
        every { shouldShowForYouHint() } returns flowOf(true)
        val expected = ForYouState(
            shouldShowHint = true,
            suggestedMovie = ForYouState.SuggestedMovie.Data(ForYouMovieUiModelSample.Inception),
            suggestedTvShow = ForYouState.SuggestedTvShow.Loading
        )

        // when
        viewModel.state.test {
            awaitLoading()

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `hint is not shown when suggestions are loading`() = runTest {
        // given
        every { shouldShowForYouHint() } returns flowOf(true)
        val expected = ForYouState(
            shouldShowHint = false,
            suggestedMovie = ForYouState.SuggestedMovie.Loading,
            suggestedTvShow = ForYouState.SuggestedTvShow.Loading
        )

        // when
        viewModel.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `hint is not shown when no suggestions`() = runTest {
        // given
        val expected = ForYouState(
            shouldShowHint = false,
            suggestedMovie = ForYouState.SuggestedMovie.NoSuggestions,
            suggestedTvShow = ForYouState.SuggestedTvShow.NoSuggestions
        )
        every { shouldShowForYouHint() } returns flowOf(true)
        every { getSuggestedMoviesWithExtras(movieExtraRefresh = any(), take = any()) } returns
            flowOf(SuggestionError.NoSuggestions.left())
        every { getSuggestedTvShowsWithExtras(tvShowExtraRefresh = any(), take = any()) } returns
            flowOf(SuggestionError.NoSuggestions.left())

        // when
        viewModel.state.test {
            awaitLoading()

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `when suggestions are loaded, state contains a movie and a tv show`() = runTest {
        // given
        val expected = ForYouState(
            shouldShowHint = false,
            suggestedMovie = ForYouState.SuggestedMovie.Data(ForYouMovieUiModelSample.Inception),
            suggestedTvShow = ForYouState.SuggestedTvShow.Data(ForYouTvShowUiModelSample.Dexter)
        )

        // when
        viewModel.state.test {
            awaitLoading()

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `when no suggestion available, state contains no suggestions state`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val expected = ForYouState(
            shouldShowHint = false,
            suggestedMovie = ForYouState.SuggestedMovie.NoSuggestions,
            suggestedTvShow = ForYouState.SuggestedTvShow.NoSuggestions
        )
        every { getSuggestedMoviesWithExtras(movieExtraRefresh = any(), take = any()) } returns
            flowOf(SuggestionError.NoSuggestions.left())
        every { getSuggestedTvShowsWithExtras(tvShowExtraRefresh = any(), take = any()) } returns
            flowOf(SuggestionError.NoSuggestions.left())

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
            shouldShowHint = false,
            suggestedMovie = ForYouState.SuggestedMovie.Error(MessageTextResTestData.NoNetworkError),
            suggestedTvShow = ForYouState.SuggestedTvShow.Error(MessageTextResTestData.NoNetworkError)
        )
        every { getSuggestedMoviesWithExtras(movieExtraRefresh = any(), take = any()) } returns
            flowOf(SuggestionError.Source(NetworkError.NoNetwork).left())
        every { getSuggestedTvShowsWithExtras(tvShowExtraRefresh = any(), take = any()) } returns
            flowOf(SuggestionError.Source(NetworkError.NoNetwork).left())

        // when
        viewModel.state.test {
            awaitLoading()

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `given suggested movie is consumed, when new suggestions, then state contains the suggestions`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val expected1 = ForYouState(
            shouldShowHint = false,
            suggestedMovie = ForYouState.SuggestedMovie.Data(ForYouMovieUiModelSample.Inception),
            suggestedTvShow = ForYouState.SuggestedTvShow.Data(ForYouTvShowUiModelSample.Dexter)
        )
        val expected2 = expected1.copy(suggestedMovie = ForYouState.SuggestedMovie.Loading)
        val expected3 = expected2.copy(
            suggestedMovie = ForYouState.SuggestedMovie.Data(ForYouMovieUiModelSample.TheWolfOfWallStreet)
        )
        val suggestedMovieFlow = MutableStateFlow(MovieWithExtrasTestData.Inception)
        every { getSuggestedMoviesWithExtras(movieExtraRefresh = any(), take = any()) } returns
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
    fun `given suggested tv show is consumed, when new suggestions, then state contains the suggestions`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val expected1 = ForYouState(
            shouldShowHint = false,
            suggestedMovie = ForYouState.SuggestedMovie.Data(ForYouMovieUiModelSample.Inception),
            suggestedTvShow = ForYouState.SuggestedTvShow.Data(ForYouTvShowUiModelSample.Grimm)
        )
        val expected2 = expected1.copy(suggestedTvShow = ForYouState.SuggestedTvShow.Loading)
        val expected3 = expected2.copy(
            suggestedTvShow = ForYouState.SuggestedTvShow.Data(ForYouTvShowUiModelSample.Dexter)
        )
        val suggestedTvShowFlow = MutableStateFlow(TvShowWithExtrasTestData.Grimm)
        every { getSuggestedTvShowsWithExtras(tvShowExtraRefresh = any(), take = any()) } returns
            suggestedTvShowFlow.map { nonEmptyListOf(it).right() }

        // when
        viewModel.state.test {
            awaitLoading()

            assertEquals(expected1, awaitItem())
            viewModel.submit(ForYouAction.Like(TmdbTvShowIdTestData.Grimm))

            assertEquals(expected2, awaitItem())
            suggestedTvShowFlow.emit(TvShowWithExtrasTestData.Dexter)

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
    fun `dislike calls the use case with the correct tv show id`() = runTest {
        // given
        val tvShowId = TmdbTvShowIdTestData.Grimm

        // when
        viewModel.submit(ForYouAction.Dislike(tvShowId))
        advanceUntilIdle()

        // then
        coVerify { addTvShowToDislikedList(tvShowId) }
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
    fun `like calls the use case with the correct tv show id`() = runTest {
        // given
        val tvShowId = TmdbTvShowIdTestData.Grimm

        // when
        viewModel.submit(ForYouAction.Like(tvShowId))
        advanceUntilIdle()

        // then
        coVerify { addTvShowToLikedList(tvShowId) }
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
    fun `add to watchlist calls the use case with the correct tv show id`() = runTest {
        // given
        val tvShowId = TmdbTvShowIdTestData.Grimm

        // when
        viewModel.submit(ForYouAction.AddToWatchlist(tvShowId))
        advanceUntilIdle()

        // then
        coVerify { addTvShowToWatchlist(tvShowId) }
    }

    @Test
    fun `suggested movie is changed after dislike`() = runTest(dispatchTimeoutMs = TestTimeout) {
        // given
        val firstState = ForYouState(
            shouldShowHint = false,
            suggestedMovie = ForYouState.SuggestedMovie.Data(ForYouMovieUiModelSample.Inception),
            suggestedTvShow = ForYouState.SuggestedTvShow.Data(ForYouTvShowUiModelSample.Dexter)
        )
        val secondState = firstState.copy(
            suggestedMovie = ForYouState.SuggestedMovie.Data(ForYouMovieUiModelSample.TheWolfOfWallStreet)
        )

        // when
        viewModel.state.test {
            awaitLoading()

            assertEquals(firstState, awaitItem())
            viewModel.submit(ForYouAction.Dislike(MovieTestData.Inception.tmdbId))

            // then
            assertEquals(secondState, awaitItem())
        }
    }

    @Test
    fun `suggested tv show is changed after dislike`() = runTest(dispatchTimeoutMs = TestTimeout) {
        // given
        val firstState = ForYouState(
            shouldShowHint = false,
            suggestedMovie = ForYouState.SuggestedMovie.Data(ForYouMovieUiModelSample.Inception),
            suggestedTvShow = ForYouState.SuggestedTvShow.Data(ForYouTvShowUiModelSample.Dexter)
        )
        val secondState = firstState.copy(
            suggestedTvShow = ForYouState.SuggestedTvShow.Data(ForYouTvShowUiModelSample.Grimm)
        )

        // when
        viewModel.state.test {
            awaitLoading()

            assertEquals(firstState, awaitItem())
            viewModel.submit(ForYouAction.Dislike(TmdbTvShowIdTestData.Dexter))

            // then
            assertEquals(secondState, awaitItem())
        }
    }

    @Test
    fun `suggested movie is changed after like`() = runTest(dispatchTimeoutMs = TestTimeout) {
        // given
        val firstState = ForYouState(
            shouldShowHint = false,
            suggestedMovie = ForYouState.SuggestedMovie.Data(ForYouMovieUiModelSample.Inception),
            suggestedTvShow = ForYouState.SuggestedTvShow.Data(ForYouTvShowUiModelSample.Dexter)
        )
        val secondState = firstState.copy(
            suggestedMovie = ForYouState.SuggestedMovie.Data(ForYouMovieUiModelSample.TheWolfOfWallStreet)
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
    fun `suggested tv show is changed after like`() = runTest(dispatchTimeoutMs = TestTimeout) {
        // given
        val firstState = ForYouState(
            shouldShowHint = false,
            suggestedMovie = ForYouState.SuggestedMovie.Data(ForYouMovieUiModelSample.Inception),
            suggestedTvShow = ForYouState.SuggestedTvShow.Data(ForYouTvShowUiModelSample.Dexter)
        )
        val secondState = firstState.copy(
            suggestedTvShow = ForYouState.SuggestedTvShow.Data(ForYouTvShowUiModelSample.Grimm)
        )

        // when
        viewModel.state.test {
            awaitLoading()

            assertEquals(firstState, awaitItem())
            viewModel.submit(ForYouAction.Like(TmdbTvShowIdTestData.Dexter))

            // then
            assertEquals(secondState, awaitItem())
        }
    }

    @Test
    fun `suggested movie is changed after add to watchlist`() = runTest(dispatchTimeoutMs = TestTimeout) {
        // given
        val firstState = ForYouState(
            shouldShowHint = false,
            suggestedMovie = ForYouState.SuggestedMovie.Data(ForYouMovieUiModelSample.Inception),
            suggestedTvShow = ForYouState.SuggestedTvShow.Data(ForYouTvShowUiModelSample.Dexter)
        )
        val secondState = firstState.copy(
            suggestedMovie = ForYouState.SuggestedMovie.Data(ForYouMovieUiModelSample.TheWolfOfWallStreet)
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

    @Test
    fun `suggested tv show is changed after add to watchlist`() = runTest(dispatchTimeoutMs = TestTimeout) {
        // given
        val firstState = ForYouState(
            shouldShowHint = false,
            suggestedMovie = ForYouState.SuggestedMovie.Data(ForYouMovieUiModelSample.Inception),
            suggestedTvShow = ForYouState.SuggestedTvShow.Data(ForYouTvShowUiModelSample.Dexter)
        )
        val secondState = firstState.copy(
            suggestedTvShow = ForYouState.SuggestedTvShow.Data(ForYouTvShowUiModelSample.Grimm)
        )

        // when
        viewModel.state.test {
            awaitLoading()

            assertEquals(firstState, awaitItem())
            viewModel.submit(ForYouAction.AddToWatchlist(TmdbTvShowIdTestData.Dexter))

            // then
            assertEquals(secondState, awaitItem())
        }
    }

    private suspend fun ReceiveTurbine<ForYouState>.awaitLoading() {
        assertEquals(awaitItem(), ForYouState.Loading)
    }
}
