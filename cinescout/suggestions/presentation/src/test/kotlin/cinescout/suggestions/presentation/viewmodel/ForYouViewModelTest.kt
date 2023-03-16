package cinescout.suggestions.presentation.viewmodel

import app.cash.turbine.test
import arrow.core.Nel
import arrow.core.nonEmptyListOf
import cinescout.design.FakeNetworkErrorToMessageMapper
import cinescout.suggestions.domain.model.SuggestedScreenplayWithExtras
import cinescout.suggestions.domain.sample.SuggestedScreenplayWithExtrasSample
import cinescout.suggestions.domain.usecase.FakeGetSuggestionsWithExtras
import cinescout.suggestions.presentation.mapper.RealForYouItemUiModelMapper
import cinescout.suggestions.presentation.model.ForYouEvent
import cinescout.suggestions.presentation.model.ForYouOperation
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.reducer.FakeForYouReducer
import cinescout.suggestions.presentation.sample.ForYouScreenplayUiModelSample
import cinescout.suggestions.presentation.util.Stack
import cinescout.test.android.ViewModelExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.shouldBe
import io.mockk.mockk

class ForYouViewModelTest : BehaviorSpec({
    extension(ViewModelExtension())
    
    val suggestions = nonEmptyListOf(
        SuggestedScreenplayWithExtrasSample.BreakingBad,
        SuggestedScreenplayWithExtrasSample.Dexter,
        SuggestedScreenplayWithExtrasSample.Inception,
        SuggestedScreenplayWithExtrasSample.TheWolfOfWallStreet
    )
    val movieStack = Stack.of(
        ForYouScreenplayUiModelSample.Inception,
        ForYouScreenplayUiModelSample.TheWolfOfWallStreet
    )
    val tvShowsStack = Stack.of(
        ForYouScreenplayUiModelSample.BreakingBad,
        ForYouScreenplayUiModelSample.Dexter
    )

    Given("ViewModel") {

        When("started") {
            val scenario = TestScenario()

            Then("state is loading") {
                scenario.sut.state.test {
                    awaitItem() shouldBe ForYouState.Loading
                }
            }
        }
    }

    Given("type is movies") {

        When("suggestions are loaded") {
            val reduce = { state: ForYouState, operation: ForYouOperation ->
                when (operation) {
                    is ForYouEvent.SuggestedMoviesReceived -> state.copy(moviesStack = movieStack)
                    else -> state
                }
            }
            val scenario = TestScenario(reduce = reduce, suggestions = suggestions)

            Then("emits a list of movies") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.sut.state.test {
                    awaitItem().moviesStack shouldBe movieStack
                }
            }
        }
    }
    
    Given("type is tv shows") {
            
        When("suggestions are loaded") {
            val reduce = { state: ForYouState, operation: ForYouOperation ->
                when (operation) {
                    is ForYouEvent.SuggestedTvShowsReceived -> state.copy(tvShowsStack = tvShowsStack)
                    else -> state
                }
            }
            val scenario = TestScenario(reduce = reduce, suggestions = suggestions)
    
            Then("emits a list of tv shows") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.sut.state.test {
                    awaitItem().tvShowsStack shouldBe tvShowsStack
                }
            }
        }
    }
})

private class ForYouViewModelTestScenario(
    val sut: ForYouViewModel
)

private fun TestScenario(
    reduce: (state: ForYouState, operation: ForYouOperation) -> ForYouState = { state, _ -> state },
    suggestions: Nel<SuggestedScreenplayWithExtras>? = null
): ForYouViewModelTestScenario {
    return ForYouViewModelTestScenario(
        sut = ForYouViewModel(
            addToWatchlist = mockk(),
            forYouItemUiModelMapper = RealForYouItemUiModelMapper(),
            getSuggestionsWithExtras = FakeGetSuggestionsWithExtras(suggestions),
            networkErrorMapper = FakeNetworkErrorToMessageMapper(),
            reducer = FakeForYouReducer(reduce),
            setDisliked = mockk(),
            setLiked = mockk()
        )
    )
}

//    @Test
//    fun `given type is movies, when no suggestion available, state contains no suggestions state`() = runTest(
//        dispatchTimeoutMs = TestTimeoutMs
//    ) {
//        // given
//        val expected = ForYouState(
//            suggestedItem = ForYouState.SuggestedItem.NoSuggestedMovies,
//            moviesStack = Stack.empty(),
//            tvShowsStack = suggestedTvShowsStack(),
//            type = ForYouType.Movies
//        )
//        every { getSuggestedMoviesWithExtras(movieExtraRefresh = any(), take = any()) } returns
//            flowOf(SuggestionError.NoSuggestions.left())
//
//        // when
//        viewModel.alsoAdvanceUntilIdle().state.test {
//
//            // then
//            assertState(expected)
//        }
//    }
//
//    @Test
//    fun `given type is tv shows, when no suggestion available, state contains no suggestions state`() =
//        runTest(
//            dispatchTimeoutMs = TestTimeoutMs
//        ) {
//            // given
//            val expected = ForYouState(
//                suggestedItem = ForYouState.SuggestedItem.NoSuggestedTvShows,
//                moviesStack = Stack.empty(),
//                tvShowsStack = Stack.empty(),
//                type = ForYouType.TvShows
//            )
//            viewModel.submit(ForYouAction.SelectForYouType(ForYouType.TvShows))
//            every { getSuggestedMoviesWithExtras(movieExtraRefresh = any(), take = any()) } returns
//                flowOf(SuggestionError.NoSuggestions.left())
//            every { getSuggestedTvShowsWithExtras(tvShowExtraRefresh = any(), take = any()) } returns
//                flowOf(SuggestionError.NoSuggestions.left())
//
//            // when
//            viewModel.state.test {
//                awaitLoading()
//
//                // then
//                assertEquals(expected, awaitItem())
//            }
//        }
//
//    @Test
//    fun `given type is movies, when error while loading suggestions, state contains the error message`() =
//        runTest(
//            dispatchTimeoutMs = TestTimeoutMs
//        ) {
//            // given
//            val expected = ForYouState(
//                suggestedItem = ForYouState.SuggestedItem.Error(MessageSample.NoNetworkError),
//                moviesStack = Stack.empty(),
//                tvShowsStack = suggestedTvShowsStack(),
//                type = ForYouType.Movies
//            )
//            every { getSuggestedMoviesWithExtras(movieExtraRefresh = any(), take = any()) } returns
//                flowOf(SuggestionError.Source(NetworkError.NoNetwork).left())
//
//            // when
//            viewModel.alsoAdvanceUntilIdle().state.test {
//
//                // then
//                assertState(expected)
//            }
//        }
//
//    @Test
//    fun `given type is tv shows, when error while loading suggestions, state contains the error message`() =
//        runTest(
//            dispatchTimeoutMs = TestTimeoutMs
//        ) {
//            // given
//            val expected = ForYouState(
//                suggestedItem = ForYouState.SuggestedItem.Error(MessageSample.NoNetworkError),
//                moviesStack = suggestedMoviesStack(),
//                tvShowsStack = Stack.empty(),
//                type = ForYouType.TvShows
//            )
//            viewModel.submit(ForYouAction.SelectForYouType(ForYouType.TvShows))
//            every { getSuggestedTvShowsWithExtras(tvShowExtraRefresh = any(), take = any()) } returns
//                flowOf(SuggestionError.Source(NetworkError.NoNetwork).left())
//
//            // when
//            viewModel.state.test {
//                awaitLoading()
//                awaitTypeChange(ForYouType.TvShows)
//                awaitItem()
//
//                // then
//                assertState(expected)
//            }
//        }
//
//    @Test
//    fun `dislike calls the use case with the correct movie id`() = runTest {
//        // given
//        val movieId = MovieSample.Inception.tmdbId
//
//        // when
//        viewModel.submit(ForYouAction.Dislike(movieId))
//        advanceUntilIdle()
//
//        // then
//        coVerify { addMovieToDislikedList(movieId) }
//    }
//
//    @Test
//    fun `dislike calls the use case with the correct tv show id`() = runTest {
//        // given
//        val tvShowId = TmdbTvShowIdSample.Grimm
//
//        // when
//        viewModel.submit(ForYouAction.Dislike(tvShowId))
//        advanceUntilIdle()
//
//        // then
//        coVerify { addTvShowToDislikedList(tvShowId) }
//    }
//
//    @Test
//    fun `like calls the use case with the correct movie id`() = runTest {
//        // given
//        val movieId = MovieSample.Inception.tmdbId
//
//        // when
//        viewModel.submit(ForYouAction.Like(movieId))
//        advanceUntilIdle()
//
//        // then
//        coVerify { addMovieToLikedList(movieId) }
//    }
//
//    @Test
//    fun `like calls the use case with the correct tv show id`() = runTest {
//        // given
//        val tvShowId = TmdbTvShowIdSample.Grimm
//
//        // when
//        viewModel.submit(ForYouAction.Like(tvShowId))
//        advanceUntilIdle()
//
//        // then
//        coVerify { addTvShowToLikedList(tvShowId) }
//    }
//
//    @Test
//    fun `add to watchlist calls the use case with the correct movie id`() = runTest {
//        // given
//        val movieId = MovieSample.Inception.tmdbId
//
//        // when
//        viewModel.submit(ForYouAction.AddToWatchlist(movieId))
//        advanceUntilIdle()
//
//        // then
//        coVerify { addMovieToWatchlist(movieId) }
//    }
//
//    @Test
//    fun `add to watchlist calls the use case with the correct tv show id`() = runTest {
//        // given
//        val tvShowId = TmdbTvShowIdSample.Grimm
//
//        // when
//        viewModel.submit(ForYouAction.AddToWatchlist(tvShowId))
//        advanceUntilIdle()
//
//        // then
//        coVerify { addTvShowToWatchlist(tvShowId) }
//    }
//
//    @Test
//    fun `suggested movie is changed after dislike`() = runTest(dispatchTimeoutMs = TestTimeoutMs) {
//        // given
//        val firstState = ForYouState(
//            suggestedItem = ForYouState.SuggestedItem.Screenplay(ForYouMovieUiModelSample.Inception),
//            moviesStack = suggestedMoviesStack(),
//            tvShowsStack = suggestedTvShowsStack(),
//            type = ForYouType.Movies
//        )
//        val secondState = firstState.copy(
//            suggestedItem = ForYouState.SuggestedItem.Screenplay(ForYouMovieUiModelSample.TheWolfOfWallStreet),
//            moviesStack = suggestedMoviesStack().pop().first
//        )
//
//        // when
//        viewModel.alsoAdvanceUntilIdle().state.test {
//
//            assertEquals(firstState, awaitItem())
//            viewModel.submit(ForYouAction.Dislike(MovieSample.Inception.tmdbId))
//
//            // then
//            assertState(secondState)
//        }
//    }
//
//    @Test
//    fun `suggested tv show is changed after dislike`() = runTest(dispatchTimeoutMs = TestTimeoutMs) {
//        // given
//        val firstState = ForYouState(
//            suggestedItem = ForYouState.SuggestedItem.Screenplay(ForYouTvShowUiModelSample.Dexter),
//            moviesStack = suggestedMoviesStack(),
//            tvShowsStack = suggestedTvShowsStack(),
//            type = ForYouType.TvShows
//        )
//        val secondState = firstState.copy(
//            suggestedItem = ForYouState.SuggestedItem.Screenplay(ForYouTvShowUiModelSample.Grimm),
//            tvShowsStack = suggestedTvShowsStack().pop().first
//        )
//        viewModel.submit(ForYouAction.SelectForYouType(ForYouType.TvShows))
//
//        // when
//        viewModel.state.test {
//            awaitLoading()
//            awaitTypeChange(ForYouType.TvShows)
//            awaitItem()
//
//            assertState(firstState)
//            viewModel.submit(ForYouAction.Dislike(TmdbTvShowIdSample.Dexter))
//
//            // then
//            assertState(secondState)
//        }
//    }
//
//    @Test
//    fun `suggested movie is changed after like`() = runTest(dispatchTimeoutMs = TestTimeoutMs) {
//        // given
//        val firstState = ForYouState(
//            suggestedItem = ForYouState.SuggestedItem.Screenplay(ForYouMovieUiModelSample.Inception),
//            moviesStack = suggestedMoviesStack(),
//            tvShowsStack = suggestedTvShowsStack(),
//            type = ForYouType.Movies
//        )
//        val secondState = firstState.copy(
//            suggestedItem = ForYouState.SuggestedItem.Screenplay(ForYouMovieUiModelSample.TheWolfOfWallStreet),
//            moviesStack = suggestedMoviesStack().pop().first
//        )
//        val movieId = MovieSample.Inception.tmdbId
//
//        // when
//        viewModel.alsoAdvanceUntilIdle().state.test {
//
//            assertState(firstState)
//            viewModel.submit(ForYouAction.Like(movieId))
//
//            // then
//            assertState(secondState)
//        }
//    }
//
//    @Test
//    fun `suggested tv show is changed after like`() = runTest(dispatchTimeoutMs = TestTimeoutMs) {
//        // given
//        val firstState = ForYouState(
//            suggestedItem = ForYouState.SuggestedItem.Screenplay(ForYouTvShowUiModelSample.Dexter),
//            moviesStack = suggestedMoviesStack(),
//            tvShowsStack = suggestedTvShowsStack(),
//            type = ForYouType.TvShows
//        )
//        val secondState = firstState.copy(
//            suggestedItem = ForYouState.SuggestedItem.Screenplay(ForYouTvShowUiModelSample.Grimm),
//            tvShowsStack = suggestedTvShowsStack().pop().first
//        )
//        viewModel.submit(ForYouAction.SelectForYouType(ForYouType.TvShows))
//
//        // when
//        viewModel.state.test {
//            awaitLoading()
//            awaitTypeChange(ForYouType.TvShows)
//            awaitItem()
//
//            assertEquals(firstState, awaitItem())
//            viewModel.submit(ForYouAction.Like(TmdbTvShowIdSample.Dexter))
//
//            // then
//            assertEquals(secondState, awaitItem())
//        }
//    }
//
//    @Test
//    fun `suggested movie is changed after add to watchlist`() = runTest(dispatchTimeoutMs = TestTimeoutMs) {
//        // given
//        val firstState = ForYouState(
//            suggestedItem = ForYouState.SuggestedItem.Screenplay(ForYouMovieUiModelSample.Inception),
//            moviesStack = suggestedMoviesStack(),
//            tvShowsStack = suggestedTvShowsStack(),
//            type = ForYouType.Movies
//        )
//        val secondState = firstState.copy(
//            suggestedItem = ForYouState.SuggestedItem.Screenplay(ForYouMovieUiModelSample.TheWolfOfWallStreet),
//            moviesStack = suggestedMoviesStack().pop().first
//        )
//        val movieId = MovieSample.Inception.tmdbId
//
//        // when
//        viewModel.alsoAdvanceUntilIdle().state.test {
//
//            assertEquals(firstState, awaitItem())
//            viewModel.submit(ForYouAction.AddToWatchlist(movieId))
//
//            // then
//            assertEquals(secondState, awaitItem())
//        }
//    }
//
//    @Test
//    fun `suggested tv show is changed after add to watchlist`() = runTest(
//        dispatchTimeoutMs = TestTimeoutMs
//    ) {
//        // given
//        val firstState = ForYouState(
//            suggestedItem = ForYouState.SuggestedItem.Screenplay(ForYouTvShowUiModelSample.Dexter),
//            moviesStack = suggestedMoviesStack(),
//            tvShowsStack = suggestedTvShowsStack(),
//            type = ForYouType.TvShows
//        )
//        val secondState = firstState.copy(
//            suggestedItem = ForYouState.SuggestedItem.Screenplay(ForYouTvShowUiModelSample.Grimm),
//            tvShowsStack = suggestedTvShowsStack().pop().first
//        )
//        viewModel.submit(ForYouAction.SelectForYouType(ForYouType.TvShows))
//
//        // when
//        viewModel.state.test {
//            awaitLoading()
//            awaitTypeChange(ForYouType.TvShows)
//            awaitItem()
//
//            assertState(firstState)
//            viewModel.submit(ForYouAction.AddToWatchlist(TmdbTvShowIdSample.Dexter))
//
//            // then
//            assertState(secondState)
//        }
//    }
//
//    private suspend fun ReceiveTurbine<ForYouState>.assertState(expected: ForYouState) {
//        val actual = awaitItem()
//        fun Boolean.emoji() = if (this) "✅" else "❌"
//        assertEquals(
//            expected,
//            actual,
//            """
//
//                Suggested item:
//                Equals: ${(expected.suggestedItem == actual.suggestedItem).emoji()}
//                Expected: ${expected.suggestedItem}
//                Actual:   ${actual.suggestedItem}
//
//                Movies stack:
//                Equals: ${(expected.moviesStack == actual.moviesStack).emoji()}
//                Expected: ${expected.moviesStack}
//                Actual:   ${actual.moviesStack}
//
//                Tv shows stack:
//                Equals: ${(expected.tvShowsStack == actual.tvShowsStack).emoji()}
//                Expected: ${expected.tvShowsStack}
//                Actual:   ${actual.tvShowsStack}
//
//                Type:
//                Equals: ${(expected.type == actual.type).emoji()}
//                Expected: ${expected.type}
//                Actual:   ${actual.type}
//
//
//            """.trimIndent()
//        )
//    }
//
//    private fun suggestedMoviesStack() = Stack.of(
//        ForYouMovieUiModelSample.Inception,
//        ForYouMovieUiModelSample.TheWolfOfWallStreet
//    )
//
//    private fun suggestedTvShowsStack() = Stack.of(
//        ForYouTvShowUiModelSample.Dexter,
//        ForYouTvShowUiModelSample.Grimm
//    )
//
//    private suspend fun ReceiveTurbine<ForYouState>.awaitLoading() {
//        assertEquals(awaitItem(), ForYouState.Loading)
//    }
//
//    private suspend fun ReceiveTurbine<ForYouState>.awaitTypeChange(type: ForYouType) {
//        assertEquals(awaitItem().type, type)
//    }
// }
