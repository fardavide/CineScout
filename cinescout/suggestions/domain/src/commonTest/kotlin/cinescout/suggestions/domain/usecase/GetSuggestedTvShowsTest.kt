// package cinescout.suggestions.domain.usecase
//
// import app.cash.turbine.test
// import arrow.core.Either
// import arrow.core.NonEmptyList
// import arrow.core.left
// import arrow.core.nonEmptyListOf
// import arrow.core.right
// import cinescout.common.model.SuggestionError
// import cinescout.error.DataError
// import cinescout.error.NetworkError
// import cinescout.suggestions.domain.model.SuggestionsMode
// import cinescout.test.kotlin.TestTimeoutMs
// import cinescout.tvshows.domain.TvShowRepository
// import cinescout.tvshows.domain.model.TvShow
// import cinescout.tvshows.domain.sample.TvShowSample
// import io.mockk.Called
// import io.mockk.coEvery
// import io.mockk.coVerify
// import io.mockk.every
// import io.mockk.mockk
// import io.mockk.verify
// import kotlinx.coroutines.delay
// import kotlinx.coroutines.flow.MutableStateFlow
// import kotlinx.coroutines.flow.flow
// import kotlinx.coroutines.flow.flowOf
// import kotlinx.coroutines.test.runTest
// import kotlinx.coroutines.test.testTimeSource
// import store.builder.emptyPagedStore
// import kotlin.test.Ignore
// import kotlin.test.Test
// import kotlin.test.assertEquals
// import kotlin.time.Duration.Companion.seconds
// import kotlin.time.measureTime
//
// class GetSuggestedTvShowsTest {
//
//    private val tvShowRepository: TvShowRepository = mockk {
//        every { getAllLikedTvShows() } returns flowOf(emptyList())
//        every { getAllRatedTvShows(refresh = any()) } returns emptyPagedStore()
//        every { getAllWatchlistTvShows(refresh = any()) } returns emptyPagedStore()
//    }
//    private val updateSuggestedTvShows: UpdateSuggestedTvShows = mockk {
//        coEvery { invoke(suggestionsMode = any()) } returns Unit.right()
//    }
//    private val getSuggestedTvShows = GetSuggestedTvShows(
//        tvShowRepository = tvShowRepository,
//        updateSuggestedTvShows = updateSuggestedTvShows,
//        updateIfSuggestionsLessThan = TestMinimumSuggestions
//    )
//
//    @Test
//    fun `gets suggestions from repository`() = runTest {
//        // given
//        val movies = nonEmptyListOf(
//            TvShowSample.BreakingBad,
//            TvShowSample.Dexter,
//            TvShowSample.Grimm
//        ).right()
//        every { tvShowRepository.getSuggestedTvShows() } returns flowOf(movies)
//
//        // when
//        getSuggestedTvShows().test {
//
//            // then
//            assertEquals(movies, awaitItem())
//            awaitComplete()
//            verify { tvShowRepository.getSuggestedTvShows() }
//        }
//    }
//
//    @Test
//    fun `updates suggestions if less than the declared threshold`() = runTest {
//        // given
//        val movies = nonEmptyListOf(
//            TvShowSample.BreakingBad,
//            TvShowSample.Grimm
//        ).right()
//        every { tvShowRepository.getSuggestedTvShows() } returns flowOf(movies)
//
//        // when
//        getSuggestedTvShows().test {
//            awaitItem()
//            awaitComplete()
//
//            // then
//            coVerify { updateSuggestedTvShows(SuggestionsMode.Quick) }
//        }
//    }
//
//    @Test
//    fun `does not updates suggestions if same or more than the declared threshold`() = runTest {
//        // given
//        val movies = nonEmptyListOf(
//            TvShowSample.BreakingBad,
//            TvShowSample.Dexter,
//            TvShowSample.Grimm
//        ).right()
//        every { tvShowRepository.getSuggestedTvShows() } returns flowOf(movies)
//
//        // when
//        getSuggestedTvShows().test {
//            awaitItem()
//            awaitComplete()
//
//            // then
//            coVerify { updateSuggestedTvShows wasNot Called }
//        }
//    }
//
//    @Test
//    @Ignore("Waiting for actors in KMP: https://github.com/Kotlin/kotlinx.coroutines/issues/87")
//    fun `does not start update while already updating`() = runTest {
//        // given
//        val moviesFlow = flow {
//            emit(nonEmptyListOf(TvShowSample.Dexter).right())
//            delay(100)
//            emit(nonEmptyListOf(TvShowSample.Grimm).right())
//        }
//        every { tvShowRepository.getSuggestedTvShows() } returns moviesFlow
//
//        // when
//        getSuggestedTvShows().test {
//            awaitItem()
//            awaitItem()
//
//            // then
//            coVerify(exactly = 1) { updateSuggestedTvShows(SuggestionsMode.Quick) }
//        }
//    }
//
//    @Test
//    fun `emits error from updating if there are no stored suggestions from repository`() = runTest {
//        // given
//        val expected = SuggestionError.Source(DataError.Remote(NetworkError.NoNetwork)).left()
//        every { tvShowRepository.getSuggestedTvShows() } returns flowOf(DataError.Local.NoCache.left())
//        coEvery { updateSuggestedTvShows(any()) } returns expected
//
//        // when
//        getSuggestedTvShows().test {
//
//            // then
//            assertEquals(expected, awaitItem())
//            awaitComplete()
//        }
//    }
//
//    @Test
//    fun `given suggestions are consumed, when new suggestions, emits new suggestions`() = runTest(
//        dispatchTimeoutMs = TestTimeoutMs
//    ) {
//        // given
//        val expected1 = nonEmptyListOf(TvShowSample.Dexter).right()
//        val expected2 = nonEmptyListOf(TvShowSample.Grimm).right()
//
//        val suggestionsFlow: MutableStateFlow<Either<DataError.Local, NonEmptyList<TvShow>>> =
//            MutableStateFlow(nonEmptyListOf(TvShowSample.Dexter).right())
//        every { tvShowRepository.getSuggestedTvShows() } returns suggestionsFlow
//
//        // when
//        getSuggestedTvShows().test {
//
//            assertEquals(expected1, awaitItem())
//            suggestionsFlow.emit(DataError.Local.NoCache.left())
//
//            // then
//            suggestionsFlow.emit(nonEmptyListOf(TvShowSample.Grimm).right())
//            assertEquals(expected2, awaitItem())
//        }
//    }
//
//    @Test
//    fun `suggestions updated doesn't block the flow`() = runTest(
//        dispatchTimeoutMs = TestTimeoutMs
//    ) {
//        // given
//        val updateTime = 10.seconds
//
//        coEvery { updateSuggestedTvShows(suggestionsMode = SuggestionsMode.Quick) } coAnswers {
//            delay(updateTime)
//            Unit.right()
//        }
//        val suggestionsFlow: MutableStateFlow<Either<DataError.Local, NonEmptyList<TvShow>>> =
//            MutableStateFlow(nonEmptyListOf(TvShowSample.Dexter).right())
//        every { tvShowRepository.getSuggestedTvShows() } returns suggestionsFlow
//
//        // when
//        getSuggestedTvShows().test {
//
//            val time = testTimeSource.measureTime {
//                assertEquals(nonEmptyListOf(TvShowSample.Dexter).right(), awaitItem())
//                suggestionsFlow.emit(nonEmptyListOf(TvShowSample.Grimm).right())
//                assertEquals(nonEmptyListOf(TvShowSample.Grimm).right(), awaitItem())
//            }
//
//            // then
//            assert(time < updateTime) { "Time should be lower than $updateTime, but is $time" }
//        }
//    }
//
//    companion object {
//
//        const val TestMinimumSuggestions = 3
//    }
// }
