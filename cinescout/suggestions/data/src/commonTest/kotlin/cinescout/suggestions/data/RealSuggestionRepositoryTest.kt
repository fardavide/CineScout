package cinescout.suggestions.data

import app.cash.turbine.test
import arrow.core.Nel
import arrow.core.left
import arrow.core.nonEmptyListOf
import arrow.core.right
import cinescout.suggestions.domain.model.SuggestedMovieId
import cinescout.suggestions.domain.model.SuggestedTvShowId
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.sample.SuggestedMovieIdSample
import cinescout.suggestions.domain.sample.SuggestedMovieSample
import cinescout.suggestions.domain.sample.SuggestedTvShowIdSample
import cinescout.suggestions.domain.sample.SuggestedTvShowSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class RealSuggestionRepositoryTest : BehaviorSpec({

    Given("suggestions are available") {
        val suggestedMovieIds = nonEmptyListOf(
            SuggestedMovieIdSample.Inception,
            SuggestedMovieIdSample.TheWolfOfWallStreet
        )
        val suggestedTvShowIds = nonEmptyListOf(
            SuggestedTvShowIdSample.BreakingBad,
            SuggestedTvShowIdSample.Dexter
        )
        val scenario = TestScenario(
            suggestedMovieIds = suggestedMovieIds,
            suggestedTvShowIds = suggestedTvShowIds
        )

        When("get suggested movies ids") {

            Then("suggestions are emitted") {
                scenario.sut.getSuggestedMovieIds().test {
                    awaitItem() shouldBe suggestedMovieIds.right()
                }
            }
        }

        When("get suggested tv shows ids") {

            Then("suggestions are emitted") {
                scenario.sut.getSuggestedTvShowIds().test {
                    awaitItem() shouldBe suggestedTvShowIds.right()
                }
            }
        }
    }

    Given("no suggestions are available") {
        val scenario = TestScenario(suggestedMovieIds = null, suggestedTvShowIds = null)

        When("get suggested movies ids") {

            Then("no suggestion is emitted") {
                scenario.sut.getSuggestedMovieIds().test {
                    awaitItem() shouldBe SuggestionError.NoSuggestions.left()
                }
            }
        }

        When("get suggested tv shows ids") {

            Then("no suggestion is emitted") {
                scenario.sut.getSuggestedTvShowIds().test {
                    awaitItem() shouldBe SuggestionError.NoSuggestions.left()
                }
            }
        }

        When("inserting movie suggestions") {
            val suggestedMovies = nonEmptyListOf(
                SuggestedMovieSample.Inception,
                SuggestedMovieSample.TheWolfOfWallStreet
            )
            val suggestedMovieIds = nonEmptyListOf(
                SuggestedMovieIdSample.Inception,
                SuggestedMovieIdSample.TheWolfOfWallStreet
            )
            scenario.sut.storeSuggestedMovies(suggestedMovies)

            Then("suggestions are emitted") {
                scenario.sut.getSuggestedMovieIds().test {
                    awaitItem() shouldBe suggestedMovieIds.right()
                }
            }
        }

        When("inserting tv show suggestions") {
            val suggestedTvShows = nonEmptyListOf(
                SuggestedTvShowSample.BreakingBad,
                SuggestedTvShowSample.Dexter
            )
            val suggestedTvShowIds = nonEmptyListOf(
                SuggestedTvShowIdSample.BreakingBad,
                SuggestedTvShowIdSample.Dexter
            )
            scenario.sut.storeSuggestedTvShows(suggestedTvShows)

            Then("suggestions are emitted") {
                scenario.sut.getSuggestedTvShowIds().test {
                    awaitItem() shouldBe suggestedTvShowIds.right()
                }
            }
        }
    }
})

private class RealSuggestionRepositoryTestScenario(
    val sut: RealSuggestionRepository
)

private fun TestScenario(
    suggestedMovieIds: Nel<SuggestedMovieId>?,
    suggestedTvShowIds: Nel<SuggestedTvShowId>?
): RealSuggestionRepositoryTestScenario {
    return RealSuggestionRepositoryTestScenario(
        sut = RealSuggestionRepository(
            localSuggestionDataSource = FakeLocalSuggestionDataSource(
                suggestedMovieIds = suggestedMovieIds,
                suggestedTvShowIds = suggestedTvShowIds
            )
        )
    )
}
