package cinescout.progress.domain.usecase

import cinescout.history.domain.model.MovieHistory
import cinescout.history.domain.model.ScreenplayHistoryItem
import cinescout.history.domain.sample.HistoryItemIdSample
import cinescout.progress.domain.model.MovieProgress
import cinescout.sample.DateTimeSample
import cinescout.screenplay.domain.sample.ScreenplaySample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestScope
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.StandardTestDispatcher

class CalculateProgressTest : BehaviorSpec({
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
})

private class CalculateProgressTestScenario(
    val sut: CalculateProgress
)

private fun TestScope.TestScenario() = CalculateProgressTestScenario(
    sut = CalculateProgress(dispatcher = StandardTestDispatcher(testCoroutineScheduler))
)
