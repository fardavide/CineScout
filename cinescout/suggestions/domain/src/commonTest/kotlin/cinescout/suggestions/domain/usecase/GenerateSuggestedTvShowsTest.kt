// package cinescout.suggestions.domain.usecase
//
// import app.cash.turbine.test
// import arrow.core.left
// import arrow.core.nonEmptyListOf
// import arrow.core.right
// import cinescout.suggestions.domain.model.SuggestionError
// import cinescout.suggestions.domain.model.SuggestionsMode
// import cinescout.tvshows.domain.TvShowRepository
// import cinescout.tvshows.domain.model.TvShow
// import cinescout.tvshows.domain.model.TvShowWithPersonalRating
// import cinescout.tvshows.domain.sample.TvShowSample
// import cinescout.tvshows.domain.sample.TvShowWithPersonalRatingSample
// import cinescout.tvshows.domain.usecase.GetAllDislikedTvShows
// import cinescout.tvshows.domain.usecase.GetAllLikedTvShows
// import cinescout.tvshows.domain.usecase.GetAllRatedTvShows
// import cinescout.tvshows.domain.usecase.GetAllWatchlistTvShows
// import io.mockk.every
// import io.mockk.mockk
// import io.mockk.spyk
// import io.mockk.verify
// import kotlinx.coroutines.flow.first
// import kotlinx.coroutines.flow.flowOf
// import kotlinx.coroutines.test.runTest
// import store.builder.emptyPagedStore
// import store.builder.pagedStoreOf
// import kotlin.test.Test
// import kotlin.test.assertEquals
//
// internal class GenerateSuggestedTvShowsTest {
//
//    private val getAllDislikedTvShows: GetAllDislikedTvShows = mockk {
//        every { this@mockk() } returns flowOf(emptyList<TvShow>())
//    }
//    private val getAllLikedTvShows: GetAllLikedTvShows = mockk {
//        every { this@mockk() } returns flowOf(emptyList<TvShow>())
//    }
//    private val getAllRatedTvShows: GetAllRatedTvShows = mockk {
//        every { this@mockk(refresh = any()) } returns emptyPagedStore()
//    }
//    private val getAllWatchlistTvShows: GetAllWatchlistTvShows = mockk {
//        every { this@mockk(refresh = any()) } returns emptyPagedStore()
//    }
//    private val tvShowRepository: TvShowRepository = mockk {
//        every { getRecommendationsFor(tvShowId = any(), refresh = any()) } returns emptyPagedStore()
//    }
//    private val generateSuggestedTvShows = GenerateSuggestedTvShows(
//        getAllDislikedTvShows = getAllDislikedTvShows,
//        getAllLikedTvShows = getAllLikedTvShows,
//        getAllRatedTvShows = getAllRatedTvShows,
//        getAllWatchlistTvShows = getAllWatchlistTvShows,
//        tvShowRepository = tvShowRepository
//    )
//
//    @Test
//    fun `quick update fetches only the first page of rated tv shows`() = runTest {
//        // given
//        val mode = SuggestionsMode.Quick
//        val suggestedTvShowsPagedStore = spyk(emptyPagedStore<TvShowWithPersonalRating>())
//        every { getAllRatedTvShows() } returns suggestedTvShowsPagedStore
//
//        // when
//        generateSuggestedTvShows(mode).first()
//
//        // then
//        verify(exactly = 0) { suggestedTvShowsPagedStore.loadAll() }
//    }
//
//    @Test
//    fun `quick update fetches only the first page of watchlist tv shows`() = runTest {
//        // given
//        val mode = SuggestionsMode.Quick
//        val suggestedTvShowsPagedStore = spyk(emptyPagedStore<TvShow>())
//        every { getAllWatchlistTvShows() } returns suggestedTvShowsPagedStore
//
//        // when
//        generateSuggestedTvShows(mode).first()
//
//        // then
//        verify(exactly = 0) { suggestedTvShowsPagedStore.loadAll() }
//    }
//
//    @Test
//    fun `deep update fetches all the pages of rated tv shows`() = runTest {
//        // given
//        val mode = SuggestionsMode.Deep
//        val suggestedTvShowsPagedStore = spyk(emptyPagedStore<TvShowWithPersonalRating>())
//        every { getAllRatedTvShows(refresh = any()) } returns suggestedTvShowsPagedStore
//
//        // when
//        generateSuggestedTvShows(mode).first()
//
//        // then
//        verify { suggestedTvShowsPagedStore.loadAll() }
//    }
//
//    @Test
//    fun `deep update fetches all the pages of watchlist tv shows`() = runTest {
//        // given
//        val mode = SuggestionsMode.Deep
//        val suggestedTvShowsPagedStore = spyk(emptyPagedStore<TvShow>())
//        every { getAllWatchlistTvShows(refresh = any()) } returns suggestedTvShowsPagedStore
//
//        // when
//        generateSuggestedTvShows(mode).first()
//
//        // then
//        verify { suggestedTvShowsPagedStore.loadAll() }
//    }
//
//    @Test
//    fun `when no positive tv shows`() = runTest {
//        // given
//        val expected = SuggestionError.NoSuggestions.left()
//        every { getAllRatedTvShows() } returns emptyPagedStore()
//
//        // when
//        generateSuggestedTvShows(SuggestionsMode.Deep).test {
//
//            // the
//            assertEquals(expected, awaitItem())
//            awaitComplete()
//        }
//    }
//
//    @Test
//    fun `excludes all the known tv shows`() = runTest {
//        // given
//        val expected = SuggestionError.NoSuggestions.left()
//        val recommendedTvShows = listOf(
//            TvShowSample.BreakingBad,
//            TvShowSample.Dexter,
//            TvShowSample.Grimm
//        )
//        every { tvShowRepository.getRecommendationsFor(tvShowId = any(), refresh = any()) } returns
//            pagedStoreOf(recommendedTvShows)
//        every { getAllDislikedTvShows() } returns flowOf(listOf(TvShowSample.BreakingBad))
//        every { getAllLikedTvShows() } returns flowOf(listOf(TvShowSample.Grimm))
//        every { getAllRatedTvShows(refresh = any()) } returns
//            pagedStoreOf(listOf(TvShowWithPersonalRatingSample.Dexter))
//
//        // when
//        generateSuggestedTvShows(SuggestionsMode.Deep).test {
//
//            // then
//            assertEquals(expected, awaitItem())
//            awaitComplete()
//        }
//    }
//
//    @Test
//    fun `when all suggestions are known tv shows`() = runTest {
//        // given
//        val expected = SuggestionError.NoSuggestions.left()
//        val recommendedTvShows = listOf(
//            TvShowSample.BreakingBad,
//            TvShowSample.Dexter,
//            TvShowSample.Grimm
//        )
//        every { tvShowRepository.getRecommendationsFor(tvShowId = any(), refresh = any()) } returns
//            pagedStoreOf(recommendedTvShows)
//        every { getAllDislikedTvShows() } returns flowOf(listOf(TvShowSample.BreakingBad))
//        every { getAllLikedTvShows() } returns flowOf(listOf(TvShowSample.Dexter))
//        every { getAllRatedTvShows(refresh = any()) } returns
//            pagedStoreOf(listOf(TvShowWithPersonalRatingSample.Grimm))
//
//        // when
//        generateSuggestedTvShows(SuggestionsMode.Deep).test {
//
//            // then
//            assertEquals(expected, awaitItem())
//            awaitComplete()
//        }
//    }
//
//    @Test
//    fun `when recommended returns empty`() = runTest {
//        // given
//        val expected = SuggestionError.NoSuggestions.left()
//        every { tvShowRepository.getRecommendationsFor(tvShowId = any(), refresh = any()) } returns emptyPagedStore()
//
//        // when
//        generateSuggestedTvShows(SuggestionsMode.Deep).test {
//
//            // then
//            assertEquals(expected, awaitItem())
//            awaitComplete()
//        }
//    }
//
//    @Test
//    fun `when success`() = runTest {
//        // given
//        val expected = nonEmptyListOf(
//            TvShowSample.BreakingBad,
//            TvShowSample.Dexter
//        ).right()
//        val recommendedTvShows = pagedStoreOf(
//            TvShowSample.BreakingBad,
//            TvShowSample.Dexter,
//            TvShowSample.Grimm
//        )
//        every { tvShowRepository.getRecommendationsFor(tvShowId = any(), refresh = any()) } returns recommendedTvShows
//        every { getAllDislikedTvShows() } returns flowOf(emptyList())
//        every { getAllLikedTvShows() } returns flowOf(emptyList())
//        every { getAllRatedTvShows(refresh = any()) } returns
//            pagedStoreOf(listOf(TvShowWithPersonalRatingSample.Grimm))
//
//        // when
//        generateSuggestedTvShows(SuggestionsMode.Deep).test {
//
//            // then
//            assertEquals(expected, awaitItem())
//            awaitComplete()
//        }
//    }
// }
