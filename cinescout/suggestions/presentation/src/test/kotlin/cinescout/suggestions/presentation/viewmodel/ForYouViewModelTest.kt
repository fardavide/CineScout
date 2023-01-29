package cinescout.suggestions.presentation.viewmodel

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import arrow.core.left
import arrow.core.nonEmptyListOf
import arrow.core.right
import cinescout.common.model.SuggestionError
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.design.testdata.MessageSample
import cinescout.error.NetworkError
import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.testdata.MovieWithExtrasTestData
import cinescout.movies.domain.usecase.AddMovieToDislikedList
import cinescout.movies.domain.usecase.AddMovieToLikedList
import cinescout.movies.domain.usecase.AddMovieToWatchlist
import cinescout.suggestions.domain.usecase.GetSuggestedMoviesWithExtras
import cinescout.suggestions.domain.usecase.GetSuggestedTvShowsWithExtras
import cinescout.suggestions.presentation.mapper.ForYouItemUiModelMapper
import cinescout.suggestions.presentation.model.ForYouAction
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.model.ForYouType
import cinescout.suggestions.presentation.reducer.ForYouReducer
import cinescout.suggestions.presentation.sample.ForYouMovieUiModelSample
import cinescout.suggestions.presentation.sample.ForYouTvShowUiModelSample
import cinescout.suggestions.presentation.util.Stack
import cinescout.suggestions.presentation.util.pop
import cinescout.test.kotlin.TestTimeout
import cinescout.test.kotlin.alsoAdvanceUntilIdle
import cinescout.tvshows.domain.sample.TmdbTvShowIdSample
import cinescout.tvshows.domain.testdata.TvShowWithExtrasTestData
import cinescout.tvshows.domain.usecase.AddTvShowToDislikedList
import cinescout.tvshows.domain.usecase.AddTvShowToLikedList
import cinescout.tvshows.domain.usecase.AddTvShowToWatchlist
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
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
        override fun toMessage(networkError: NetworkError) = MessageSample.NoNetworkError
    }
    private val reducer = ForYouReducer()
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
            reducer = reducer,
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
    fun `given type is movies when suggestions are loaded, state contains a movie`() = runTest {
        // given
        val expected = ForYouState(
            suggestedItem = ForYouState.SuggestedItem.Screenplay(ForYouMovieUiModelSample.Inception),
            moviesStack = suggestedMoviesStack(),
            tvShowsStack = suggestedTvShowsStack(),
            type = ForYouType.Movies
        )

        // when
        viewModel.alsoAdvanceUntilIdle().state.test {

            // then
            assertState(expected)
        }
    }

    @Test
    fun `given type is tv shows when suggestions are loaded, state contains a tv show`() = runTest {
        // given
        val expected = ForYouState(
            suggestedItem = ForYouState.SuggestedItem.Screenplay(ForYouTvShowUiModelSample.Dexter),
            moviesStack = suggestedMoviesStack(),
            tvShowsStack = suggestedTvShowsStack(),
            type = ForYouType.TvShows
        )
        viewModel.submit(ForYouAction.SelectForYouType(ForYouType.TvShows))

        // when
        viewModel.state.test {
            awaitLoading()
            awaitTypeChange(ForYouType.TvShows)
            awaitItem()

            // then
            assertState(expected)
        }
    }

    @Test
    fun `given type was movie, when changed to tv show, state contains a tv show`() = runTest {
        // given
        val expected = ForYouState(
            suggestedItem = ForYouState.SuggestedItem.Screenplay(ForYouTvShowUiModelSample.Dexter),
            moviesStack = suggestedMoviesStack(),
            tvShowsStack = suggestedTvShowsStack(),
            type = ForYouType.TvShows
        )

        viewModel.state.test {
            awaitLoading()

            // when
            viewModel.submit(ForYouAction.SelectForYouType(ForYouType.TvShows))
            awaitTypeChange(ForYouType.TvShows)
            awaitItem()

            // then
            assertState(expected)
        }
    }

    @Test
    fun `given type was tv show, when changed to movie, state contains a movie`() = runTest {
        // given
        val expected = ForYouState(
            suggestedItem = ForYouState.SuggestedItem.Screenplay(ForYouMovieUiModelSample.Inception),
            moviesStack = suggestedMoviesStack(),
            tvShowsStack = suggestedTvShowsStack(),
            type = ForYouType.Movies
        )
        viewModel.submit(ForYouAction.SelectForYouType(ForYouType.TvShows))

        viewModel.alsoAdvanceUntilIdle().state.test {
            awaitTypeChange(ForYouType.TvShows)

            // when
            viewModel.submit(ForYouAction.SelectForYouType(ForYouType.Movies))

            // then
            assertState(expected)
        }
    }

    @Test
    fun `given type is movies, when no suggestion available, state contains no suggestions state`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val expected = ForYouState(
            suggestedItem = ForYouState.SuggestedItem.NoSuggestedMovies,
            moviesStack = Stack.empty(),
            tvShowsStack = suggestedTvShowsStack(),
            type = ForYouType.Movies
        )
        every { getSuggestedMoviesWithExtras(movieExtraRefresh = any(), take = any()) } returns
            flowOf(SuggestionError.NoSuggestions.left())

        // when
        viewModel.alsoAdvanceUntilIdle().state.test {

            // then
            assertState(expected)
        }
    }

    @Test
    fun `given type is tv shows, when no suggestion available, state contains no suggestions state`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val expected = ForYouState(
            suggestedItem = ForYouState.SuggestedItem.NoSuggestedTvShows,
            moviesStack = Stack.empty(),
            tvShowsStack = Stack.empty(),
            type = ForYouType.TvShows
        )
        viewModel.submit(ForYouAction.SelectForYouType(ForYouType.TvShows))
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
    fun `given type is movies, when error while loading suggestions, state contains the error message`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val expected = ForYouState(
            suggestedItem = ForYouState.SuggestedItem.Error(MessageSample.NoNetworkError),
            moviesStack = Stack.empty(),
            tvShowsStack = suggestedTvShowsStack(),
            type = ForYouType.Movies
        )
        every { getSuggestedMoviesWithExtras(movieExtraRefresh = any(), take = any()) } returns
            flowOf(SuggestionError.Source(NetworkError.NoNetwork).left())

        // when
        viewModel.alsoAdvanceUntilIdle().state.test {

            // then
            assertState(expected)
        }
    }

    @Test
    fun `given type is tv shows, when error while loading suggestions, state contains the error message`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val expected = ForYouState(
            suggestedItem = ForYouState.SuggestedItem.Error(MessageSample.NoNetworkError),
            moviesStack = suggestedMoviesStack(),
            tvShowsStack = Stack.empty(),
            type = ForYouType.TvShows
        )
        viewModel.submit(ForYouAction.SelectForYouType(ForYouType.TvShows))
        every { getSuggestedTvShowsWithExtras(tvShowExtraRefresh = any(), take = any()) } returns
            flowOf(SuggestionError.Source(NetworkError.NoNetwork).left())

        // when
        viewModel.state.test {
            awaitLoading()
            awaitTypeChange(ForYouType.TvShows)
            awaitItem()

            // then
            assertState(expected)
        }
    }

    @Test
    fun `dislike calls the use case with the correct movie id`() = runTest {
        // given
        val movieId = MovieSample.Inception.tmdbId

        // when
        viewModel.submit(ForYouAction.Dislike(movieId))
        advanceUntilIdle()

        // then
        coVerify { addMovieToDislikedList(movieId) }
    }

    @Test
    fun `dislike calls the use case with the correct tv show id`() = runTest {
        // given
        val tvShowId = TmdbTvShowIdSample.Grimm

        // when
        viewModel.submit(ForYouAction.Dislike(tvShowId))
        advanceUntilIdle()

        // then
        coVerify { addTvShowToDislikedList(tvShowId) }
    }

    @Test
    fun `like calls the use case with the correct movie id`() = runTest {
        // given
        val movieId = MovieSample.Inception.tmdbId

        // when
        viewModel.submit(ForYouAction.Like(movieId))
        advanceUntilIdle()

        // then
        coVerify { addMovieToLikedList(movieId) }
    }

    @Test
    fun `like calls the use case with the correct tv show id`() = runTest {
        // given
        val tvShowId = TmdbTvShowIdSample.Grimm

        // when
        viewModel.submit(ForYouAction.Like(tvShowId))
        advanceUntilIdle()

        // then
        coVerify { addTvShowToLikedList(tvShowId) }
    }

    @Test
    fun `add to watchlist calls the use case with the correct movie id`() = runTest {
        // given
        val movieId = MovieSample.Inception.tmdbId

        // when
        viewModel.submit(ForYouAction.AddToWatchlist(movieId))
        advanceUntilIdle()

        // then
        coVerify { addMovieToWatchlist(movieId) }
    }

    @Test
    fun `add to watchlist calls the use case with the correct tv show id`() = runTest {
        // given
        val tvShowId = TmdbTvShowIdSample.Grimm

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
            suggestedItem = ForYouState.SuggestedItem.Screenplay(ForYouMovieUiModelSample.Inception),
            moviesStack = suggestedMoviesStack(),
            tvShowsStack = suggestedTvShowsStack(),
            type = ForYouType.Movies
        )
        val secondState = firstState.copy(
            suggestedItem = ForYouState.SuggestedItem.Screenplay(ForYouMovieUiModelSample.TheWolfOfWallStreet),
            moviesStack = suggestedMoviesStack().pop().first
        )

        // when
        viewModel.alsoAdvanceUntilIdle().state.test {

            assertEquals(firstState, awaitItem())
            viewModel.submit(ForYouAction.Dislike(MovieSample.Inception.tmdbId))

            // then
            assertState(secondState)
        }
    }

    @Test
    fun `suggested tv show is changed after dislike`() = runTest(dispatchTimeoutMs = TestTimeout) {
        // given
        val firstState = ForYouState(
            suggestedItem = ForYouState.SuggestedItem.Screenplay(ForYouTvShowUiModelSample.Dexter),
            moviesStack = suggestedMoviesStack(),
            tvShowsStack = suggestedTvShowsStack(),
            type = ForYouType.TvShows
        )
        val secondState = firstState.copy(
            suggestedItem = ForYouState.SuggestedItem.Screenplay(ForYouTvShowUiModelSample.Grimm),
            tvShowsStack = suggestedTvShowsStack().pop().first
        )
        viewModel.submit(ForYouAction.SelectForYouType(ForYouType.TvShows))

        // when
        viewModel.state.test {
            awaitLoading()
            awaitTypeChange(ForYouType.TvShows)
            awaitItem()

            assertState(firstState)
            viewModel.submit(ForYouAction.Dislike(TmdbTvShowIdSample.Dexter))

            // then
            assertState(secondState)
        }
    }

    @Test
    fun `suggested movie is changed after like`() = runTest(dispatchTimeoutMs = TestTimeout) {
        // given
        val firstState = ForYouState(
            suggestedItem = ForYouState.SuggestedItem.Screenplay(ForYouMovieUiModelSample.Inception),
            moviesStack = suggestedMoviesStack(),
            tvShowsStack = suggestedTvShowsStack(),
            type = ForYouType.Movies
        )
        val secondState = firstState.copy(
            suggestedItem = ForYouState.SuggestedItem.Screenplay(ForYouMovieUiModelSample.TheWolfOfWallStreet),
            moviesStack = suggestedMoviesStack().pop().first
        )
        val movieId = MovieSample.Inception.tmdbId

        // when
        viewModel.alsoAdvanceUntilIdle().state.test {

            assertState(firstState)
            viewModel.submit(ForYouAction.Like(movieId))

            // then
            assertState(secondState)
        }
    }

    @Test
    fun `suggested tv show is changed after like`() = runTest(dispatchTimeoutMs = TestTimeout) {
        // given
        val firstState = ForYouState(
            suggestedItem = ForYouState.SuggestedItem.Screenplay(ForYouTvShowUiModelSample.Dexter),
            moviesStack = suggestedMoviesStack(),
            tvShowsStack = suggestedTvShowsStack(),
            type = ForYouType.TvShows
        )
        val secondState = firstState.copy(
            suggestedItem = ForYouState.SuggestedItem.Screenplay(ForYouTvShowUiModelSample.Grimm),
            tvShowsStack = suggestedTvShowsStack().pop().first
        )
        viewModel.submit(ForYouAction.SelectForYouType(ForYouType.TvShows))

        // when
        viewModel.state.test {
            awaitLoading()
            awaitTypeChange(ForYouType.TvShows)
            awaitItem()

            assertEquals(firstState, awaitItem())
            viewModel.submit(ForYouAction.Like(TmdbTvShowIdSample.Dexter))

            // then
            assertEquals(secondState, awaitItem())
        }
    }

    @Test
    fun `suggested movie is changed after add to watchlist`() = runTest(dispatchTimeoutMs = TestTimeout) {
        // given
        val firstState = ForYouState(
            suggestedItem = ForYouState.SuggestedItem.Screenplay(ForYouMovieUiModelSample.Inception),
            moviesStack = suggestedMoviesStack(),
            tvShowsStack = suggestedTvShowsStack(),
            type = ForYouType.Movies
        )
        val secondState = firstState.copy(
            suggestedItem = ForYouState.SuggestedItem.Screenplay(ForYouMovieUiModelSample.TheWolfOfWallStreet),
            moviesStack = suggestedMoviesStack().pop().first
        )
        val movieId = MovieSample.Inception.tmdbId

        // when
        viewModel.alsoAdvanceUntilIdle().state.test {

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
            suggestedItem = ForYouState.SuggestedItem.Screenplay(ForYouTvShowUiModelSample.Dexter),
            moviesStack = suggestedMoviesStack(),
            tvShowsStack = suggestedTvShowsStack(),
            type = ForYouType.TvShows
        )
        val secondState = firstState.copy(
            suggestedItem = ForYouState.SuggestedItem.Screenplay(ForYouTvShowUiModelSample.Grimm),
            tvShowsStack = suggestedTvShowsStack().pop().first
        )
        viewModel.submit(ForYouAction.SelectForYouType(ForYouType.TvShows))

        // when
        viewModel.state.test {
            awaitLoading()
            awaitTypeChange(ForYouType.TvShows)
            awaitItem()

            assertState(firstState)
            viewModel.submit(ForYouAction.AddToWatchlist(TmdbTvShowIdSample.Dexter))

            // then
            assertState(secondState)
        }
    }

    private suspend fun ReceiveTurbine<ForYouState>.assertState(expected: ForYouState) {
        val actual = awaitItem()
        fun Boolean.emoji() = if (this) "✅" else "❌"
        assertEquals(
            expected,
            actual,
            """
                
                Suggested item:
                Equals: ${(expected.suggestedItem == actual.suggestedItem).emoji()}
                Expected: ${expected.suggestedItem}
                Actual:   ${actual.suggestedItem}
                
                Movies stack:
                Equals: ${(expected.moviesStack == actual.moviesStack).emoji()}
                Expected: ${expected.moviesStack}
                Actual:   ${actual.moviesStack}
                
                Tv shows stack:
                Equals: ${(expected.tvShowsStack == actual.tvShowsStack).emoji()}
                Expected: ${expected.tvShowsStack}
                Actual:   ${actual.tvShowsStack}
                
                Type:
                Equals: ${(expected.type == actual.type).emoji()}
                Expected: ${expected.type}
                Actual:   ${actual.type}
                
                
            """.trimIndent()
        )
    }

    private fun suggestedMoviesStack() = Stack.of(
        ForYouMovieUiModelSample.Inception,
        ForYouMovieUiModelSample.TheWolfOfWallStreet
    )

    private fun suggestedTvShowsStack() = Stack.of(
        ForYouTvShowUiModelSample.Dexter,
        ForYouTvShowUiModelSample.Grimm
    )

    private suspend fun ReceiveTurbine<ForYouState>.awaitLoading() {
        assertEquals(awaitItem(), ForYouState.Loading)
    }

    private suspend fun ReceiveTurbine<ForYouState>.awaitTypeChange(type: ForYouType) {
        assertEquals(awaitItem().type, type)
    }
}
