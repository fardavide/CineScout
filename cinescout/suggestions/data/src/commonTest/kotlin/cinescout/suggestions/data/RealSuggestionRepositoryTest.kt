package cinescout.suggestions.data

import app.cash.turbine.test
import arrow.core.Nel
import arrow.core.left
import arrow.core.nonEmptyListOf
import arrow.core.right
import cinescout.suggestions.domain.model.SuggestedMovieId
import cinescout.suggestions.domain.model.SuggestedTvShowId
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.sample.SuggestedScreenplayIdSample
import cinescout.suggestions.domain.sample.SuggestedScreenplaySample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class RealSuggestionRepositoryTest : BehaviorSpec({

    Given("suggestions are available") {
        val suggestedMovieIds = nonEmptyListOf(
            SuggestedScreenplayIdSample.Inception,
            SuggestedScreenplayIdSample.TheWolfOfWallStreet
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
                SuggestedScreenplayIdSample.Inception,
                SuggestedScreenplayIdSample.TheWolfOfWallStreet
            )
            scenario.sut.storeSuggestions(suggestedMovies)

            Then("suggestions are emitted") {
                scenario.sut.getSuggestedMovieIds().test {
                    awaitItem() shouldBe suggestedMovieIds.right()
                }
            }
        }

        When("inserting tv show suggestions") {
            val suggestedTvShows = nonEmptyListOf(
                SuggestedScreenplaySample.BreakingBad,
                SuggestedScreenplaySample.Dexter
            )
            val suggestedTvShowIds = nonEmptyListOf(
                SuggestedTvShowIdSample.BreakingBad,
                SuggestedTvShowIdSample.Dexter
            )
            scenario.sut.storeSuggestions(suggestedTvShows)

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
