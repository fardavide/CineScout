package cinescout.suggestions.domain.usecase

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.left
import arrow.core.nonEmptyListOf
import arrow.core.right
import cinescout.common.model.SuggestionError
import cinescout.test.kotlin.TestTimeout
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.testdata.TvShowTestData
import cinescout.tvshows.domain.testdata.TvShowWithExtrasTestData
import cinescout.tvshows.domain.usecase.GetTvShowExtras
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetSuggestedTvShowsWithExtrasTest {

    private val getSuggestedTvShows: GetSuggestedTvShows = mockk {
        val movies = nonEmptyListOf(
            TvShowTestData.BreakingBad,
            TvShowTestData.Dexter,
            TvShowTestData.Grimm
        )
        every { this@mockk() } returns flowOf(movies.right())
    }
    private val getTvShowExtras: GetTvShowExtras = mockk {
        every { this@mockk(tvShow = TvShowTestData.BreakingBad, refresh = any()) } returns
            flowOf(TvShowWithExtrasTestData.BreakingBad.right())

        every { this@mockk(tvShow = TvShowTestData.Dexter, refresh = any()) } returns
            flowOf(TvShowWithExtrasTestData.Dexter.right())

        every { this@mockk(tvShow = TvShowTestData.Grimm, refresh = any()) } returns
            flowOf(TvShowWithExtrasTestData.Grimm.right())
    }
    private val getSuggestedTvShowsWithExtras = GetSuggestedTvShowsWithExtras(
        getSuggestedTvShows = getSuggestedTvShows,
        getTvShowExtras = getTvShowExtras
    )

    @Test
    fun `progressively load extras`() = runTest {
        // given
        val expected = listOf(
            nonEmptyListOf(
                TvShowWithExtrasTestData.BreakingBad
            ),
            nonEmptyListOf(
                TvShowWithExtrasTestData.BreakingBad,
                TvShowWithExtrasTestData.Dexter
            ),
            nonEmptyListOf(
                TvShowWithExtrasTestData.BreakingBad,
                TvShowWithExtrasTestData.Dexter,
                TvShowWithExtrasTestData.Grimm
            )
        )
        every { getTvShowExtras(tvShow = TvShowTestData.BreakingBad, refresh = any()) } returns flow {
            delay(100)
            emit(TvShowWithExtrasTestData.BreakingBad.right())
        }
        every { getTvShowExtras(tvShow = TvShowTestData.Dexter, refresh = any()) } returns flow {
            delay(200)
            emit(TvShowWithExtrasTestData.Dexter.right())
        }
        every { getTvShowExtras(tvShow = TvShowTestData.Grimm, refresh = any()) } returns flow {
            delay(300)
            emit(TvShowWithExtrasTestData.Grimm.right())
        }

        // when
        getSuggestedTvShowsWithExtras().test {

            // then
            assertEquals(expected[0].right(), awaitItem())
            assertEquals(expected[1].right(), awaitItem())
            assertEquals(expected[2].right(), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `given suggestions are consumed, when new suggestions, emits new suggestions`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val expected1 = nonEmptyListOf(TvShowWithExtrasTestData.BreakingBad).right()
        val expected2 = SuggestionError.NoSuggestions.left()
        val expected3 = nonEmptyListOf(TvShowWithExtrasTestData.Dexter).right()

        val suggestionsFlow: MutableStateFlow<Either<SuggestionError, NonEmptyList<TvShow>>> =
            MutableStateFlow(nonEmptyListOf(TvShowTestData.BreakingBad).right())
        every { getSuggestedTvShows() } returns suggestionsFlow

        // when
        getSuggestedTvShowsWithExtras().test {

            assertEquals(expected1, awaitItem())
            suggestionsFlow.emit(SuggestionError.NoSuggestions.left())
            assertEquals(expected2, awaitItem())

            // then
            suggestionsFlow.emit(nonEmptyListOf(TvShowTestData.Dexter).right())
            assertEquals(expected3, awaitItem())
        }
    }

    @Test
    fun `get details only for movies to take`() = runTest {
        // given
        val expected = nonEmptyListOf(
            TvShowWithExtrasTestData.BreakingBad,
            TvShowWithExtrasTestData.Dexter
        ).right()

        // when
        getSuggestedTvShowsWithExtras(take = 2).test {

            // then
            assertEquals(expected, awaitItem())
            coVerify(exactly = 1) { getTvShowExtras(tvShow = TvShowTestData.BreakingBad, refresh = any()) }
            coVerify(exactly = 1) { getTvShowExtras(tvShow = TvShowTestData.Dexter, refresh = any()) }
            coVerify(exactly = 0) { getTvShowExtras(tvShow = TvShowTestData.Grimm, refresh = any()) }
            awaitComplete()
        }
    }
}
