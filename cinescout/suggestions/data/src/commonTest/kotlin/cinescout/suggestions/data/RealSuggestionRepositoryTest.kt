package cinescout.suggestions.data

import app.cash.turbine.test
import arrow.core.Nel
import arrow.core.left
import arrow.core.nonEmptyListOf
import arrow.core.right
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.settings.domain.usecase.FakeGetSuggestionSettings
import cinescout.suggestions.domain.model.SuggestedScreenplayId
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.sample.SuggestedScreenplayIdSample
import cinescout.suggestions.domain.sample.SuggestedScreenplaySample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class RealSuggestionRepositoryTest : BehaviorSpec({

    Given("suggestions are available") {
        val suggestionIds = nonEmptyListOf(
            SuggestedScreenplayIdSample.BreakingBad,
            SuggestedScreenplayIdSample.Dexter,
            SuggestedScreenplayIdSample.Inception,
            SuggestedScreenplayIdSample.TheWolfOfWallStreet
        )
        val scenario = TestScenario(suggestionIds = suggestionIds)

        When("get suggestions ids") {

            Then("suggestions are emitted") {
                scenario.sut.getSuggestionIds(ScreenplayTypeFilter.All).test {
                    awaitItem() shouldBe suggestionIds.right()
                }
            }
        }
    }

    Given("no suggestions are available") {

        When("get suggestions ids") {
            val scenario = TestScenario(suggestionIds = null)

            Then("no suggestion is emitted") {
                scenario.sut.getSuggestionIds(ScreenplayTypeFilter.All).test {
                    awaitItem() shouldBe SuggestionError.NoSuggestions.left()
                }
            }
        }

        When("inserting movie suggestions") {
            val scenario = TestScenario(suggestionIds = null)
            val suggestedMovies = nonEmptyListOf(
                SuggestedScreenplaySample.Inception,
                SuggestedScreenplaySample.TheWolfOfWallStreet
            )
            val suggestedMovieIds = nonEmptyListOf(
                SuggestedScreenplayIdSample.Inception,
                SuggestedScreenplayIdSample.TheWolfOfWallStreet
            )
            scenario.sut.storeSuggestions(suggestedMovies)

            Then("suggestions are emitted") {
                scenario.sut.getSuggestionIds(ScreenplayTypeFilter.All).test {
                    awaitItem() shouldBe suggestedMovieIds.right()
                }
            }
        }

        When("inserting tv show suggestions") {
            val scenario = TestScenario(suggestionIds = null)
            val suggestedTvShows = nonEmptyListOf(
                SuggestedScreenplaySample.BreakingBad,
                SuggestedScreenplaySample.Dexter
            )
            val suggestedTvShowIds = nonEmptyListOf(
                SuggestedScreenplayIdSample.BreakingBad,
                SuggestedScreenplayIdSample.Dexter
            )
            scenario.sut.storeSuggestions(suggestedTvShows)

            Then("suggestions are emitted") {
                scenario.sut.getSuggestionIds(ScreenplayTypeFilter.All).test {
                    awaitItem() shouldBe suggestedTvShowIds.right()
                }
            }
        }
    }
})

private class RealSuggestionRepositoryTestScenario(
    val sut: RealSuggestionRepository
)

private fun TestScenario(suggestionIds: Nel<SuggestedScreenplayId>?): RealSuggestionRepositoryTestScenario {
    return RealSuggestionRepositoryTestScenario(
        sut = RealSuggestionRepository(
            localSuggestionDataSource = FakeLocalSuggestionDataSource(suggestionIds = suggestionIds),
            getSuggestionSettings = FakeGetSuggestionSettings()
        )
    )
}
