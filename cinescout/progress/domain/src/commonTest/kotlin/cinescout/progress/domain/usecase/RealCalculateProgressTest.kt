package cinescout.progress.domain.usecase

import cinescout.history.domain.model.MovieHistory
import cinescout.history.domain.model.ScreenplayHistoryItem
import cinescout.history.domain.model.TvShowHistory
import cinescout.history.domain.sample.HistoryItemIdSample
import cinescout.history.domain.sample.HistorySample
import cinescout.progress.domain.model.MovieProgress
import cinescout.progress.domain.sample.TvShowProgressSample
import cinescout.sample.DateTimeSample
import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.seasons.domain.sample.TvShowSeasonsWithEpisodesSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestScope
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.StandardTestDispatcher

class RealCalculateProgressTest : BehaviorSpec({
    coroutineTestScope = true

    Given("a movie and its history") {
        val movie = ScreenplaySample.Inception

        When("the history is empty") {
            val scenario = TestScenario()
            val history = MovieHistory.empty(movie.ids)
            val result = scenario.sut(movie, history)

            Then("the progress is unwatched") {
                result shouldBe MovieProgress.Unwatched(movie)
            }
        }

        When("the history has one item") {
            val scenario = TestScenario()
            val history = MovieHistory(
                items = listOf(
                    ScreenplayHistoryItem.Movie(
                        id = HistoryItemIdSample.Inception,
                        watchedAt = DateTimeSample.Xmas2023
                    )
                ),
                screenplayIds = movie.ids
            )
            val result = scenario.sut(movie, history)

            Then("the progress is watched") {
                result.shouldBeInstanceOf<MovieProgress.Watched>()
            }

            And("the count is 1") {
                (result as MovieProgress.Watched).count shouldBe 1
            }
        }

        When("the history has two items") {
            val scenario = TestScenario()
            val history = MovieHistory(
                items = listOf(
                    ScreenplayHistoryItem.Movie(
                        id = HistoryItemIdSample.Inception,
                        watchedAt = DateTimeSample.Xmas2023
                    ),
                    ScreenplayHistoryItem.Movie(
                        id = HistoryItemIdSample.Inception,
                        watchedAt = DateTimeSample.Xmas2024
                    )
                ),
                screenplayIds = movie.ids
            )
            val result = scenario.sut(movie, history)

            Then("the progress is watched") {
                result.shouldBeInstanceOf<MovieProgress.Watched>()
            }

            And("the count is 2") {
                (result as MovieProgress.Watched).count shouldBe 2
            }
        }
    }

    Given("a tv show and its history") {
        val tvShow = ScreenplaySample.BreakingBad
        val seasons = TvShowSeasonsWithEpisodesSample.BreakingBad

        When("the history is empty") {
            val scenario = TestScenario()
            val history = TvShowHistory.empty(tvShow.ids)
            val result = scenario.sut(tvShow, seasons, history)

            Then("the progress is unwatched") {
                result shouldBe TvShowProgressSample.BreakingBad_Unwatched
            }
        }

        When("history contains every episode, just once") {
            val scenario = TestScenario()
            val history = HistorySample.BreakingBad_AllWatchedOnce
            val result = scenario.sut(tvShow, seasons, history)

            Then("the progress is completed") {
                result shouldBe TvShowProgressSample.BreakingBad_Completed
            }
        }

        When("history contains every episode, without specials") {
            val scenario = TestScenario()
            val history = HistorySample.BreakingBad_WatchedOnceWithoutSpecials
            val result = scenario.sut(tvShow, seasons, history)

            Then("the progress is completed") {
                result shouldBe TvShowProgressSample.BreakingBad_CompletedWithoutSpecials
            }
        }

        When("history contains one seasons out of two") {
            val scenario = TestScenario()
            val history = HistorySample.BreakingBad_InProgress_OneSeasonCompleted
            val result = scenario.sut(tvShow, seasons, history)

            Then("the progress is completed") {
                result shouldBe TvShowProgressSample.BreakingBad_InProgress_OneSeasonWatched
            }
        }

        When("history contains one seasons out of two, plus one episode out of three") {
            val scenario = TestScenario()
            val history = HistorySample.BreakingBad_InProgress_OneSeasonCompletedAndOneEpisodeWatched
            val result = scenario.sut(tvShow, seasons, history)

            Then("the progress is completed") {
                result shouldBe TvShowProgressSample.BreakingBad_InProgress_OneSeasonAndOneEpisodeWatched
            }
        }
    }
})

private class CalculateProgressTestScenario(
    val sut: CalculateProgress
)

private fun TestScope.TestScenario() = CalculateProgressTestScenario(
    sut = RealCalculateProgress(dispatcher = StandardTestDispatcher(testCoroutineScheduler))
)
