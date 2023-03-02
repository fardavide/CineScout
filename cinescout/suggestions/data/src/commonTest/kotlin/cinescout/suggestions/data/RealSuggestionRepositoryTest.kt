package cinescout.suggestions.data

import app.cash.turbine.test
import arrow.core.Nel
import arrow.core.left
import arrow.core.nonEmptyListOf
import arrow.core.right
import cinescout.suggestions.domain.model.SuggestedMovie
import cinescout.suggestions.domain.model.SuggestedTvShow
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.sample.SuggestedMovieSample
import cinescout.suggestions.domain.sample.SuggestedTvShowSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class RealSuggestionRepositoryTest : BehaviorSpec({

    Given("suggestions are available") {
        val suggestedMovies = nonEmptyListOf(
            SuggestedMovieSample.Inception,
            SuggestedMovieSample.TheWolfOfWallStreet
        )
        val suggestedTvShows = nonEmptyListOf(
            SuggestedTvShowSample.BreakingBad,
            SuggestedTvShowSample.Dexter
        )
        val scenario = TestScenario(
            suggestedMovies = suggestedMovies,
            suggestedTvShows = suggestedTvShows
        )

        When("get suggested movies") {

            Then("suggestions are emitted") {
                scenario.sut.getSuggestedMovies().test {
                    awaitItem() shouldBe suggestedMovies.right()
                    awaitComplete()
                }
            }
        }

        When("get suggested tv shows") {

            Then("suggestions are emitted") {
                scenario.sut.getSuggestedTvShows().test {
                    awaitItem() shouldBe suggestedTvShows.right()
                    awaitComplete()
                }
            }
        }
    }

    Given("no suggestions are available") {
        val scenario = TestScenario(suggestedMovies = null, suggestedTvShows = null)

        When("get suggested movies") {

            Then("no suggestion is emitted") {
                scenario.sut.getSuggestedMovies().test {
                    awaitItem() shouldBe SuggestionError.NoSuggestions.left()
                    awaitComplete()
                }
            }
        }

        When("get suggested tv shows") {

            Then("no suggestion is emitted") {
                scenario.sut.getSuggestedTvShows().test {
                    awaitItem() shouldBe SuggestionError.NoSuggestions.left()
                    awaitComplete()
                }
            }
        }
    }
})

private class RealSuggestionRepositoryTestScenario(
    val sut: RealSuggestionRepository
)

private fun TestScenario(
    suggestedMovies: Nel<SuggestedMovie>?,
    suggestedTvShows: Nel<SuggestedTvShow>?
): RealSuggestionRepositoryTestScenario {
    return RealSuggestionRepositoryTestScenario(
        sut = RealSuggestionRepository(
            localSuggestionDataSource = FakeLocalSuggestionDataSource(
                suggestedMovies = suggestedMovies,
                suggestedTvShows = suggestedTvShows
            )
        )
    )
}
