package cinescout.suggestions.domain.usecase

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.Nel
import arrow.core.left
import arrow.core.nonEmptyListOf
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import cinescout.screenplay.domain.store.FakeRecommendedScreenplayIdsStore
import cinescout.suggestions.domain.FakeSuggestionRepository
import cinescout.suggestions.domain.model.SuggestedScreenplay
import cinescout.suggestions.domain.model.SuggestedScreenplayId
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.model.SuggestionSource
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.sample.SuggestedScreenplayIdSample
import cinescout.suggestions.domain.sample.SuggestedScreenplaySample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.shouldBe

class RealUpdateSuggestionsTest : BehaviorSpec({

    Given("updating suggestions") {
        val suggestions = nonEmptyListOf(SuggestedScreenplaySample.BreakingBad, SuggestedScreenplaySample.Inception)
        val recommendations = nonEmptyListOf(TmdbScreenplayIdSample.Dexter, TmdbScreenplayIdSample.TheWolfOfWallStreet)

        val networkError = NetworkError.Unknown
        When("generate suggestions is error") {
            val scenario = TestScenario(
                generateSuggestionsResult = SuggestionError.Source(networkError).left(),
                getRecommendationsResult = recommendations.right()
            )

            Then("error is emitted") {
                scenario.sut(ScreenplayType.All, SuggestionsMode.Quick) shouldBe networkError.left()
            }
        }

        When("get recommended is error") {
            val scenario = TestScenario(
                generateSuggestionsResult = suggestions.right(),
                getRecommendationsResult = networkError.left()
            )

            Then("error is emitted") {
                scenario.sut(ScreenplayType.All, SuggestionsMode.Quick) shouldBe networkError.left()
            }
        }

        When("all are success") {
            val scenario = TestScenario(
                generateSuggestionsResult = suggestions.right(),
                getRecommendationsResult = recommendations.right()
            )

            val result = scenario.sut(ScreenplayType.All, SuggestionsMode.Quick)

            Then("unit is returned") {
                result shouldBe Unit.right()
            }

            Then("suggested and recommended are stored") {
                scenario.suggestionRepository.getSuggestionIds().test {
                    awaitItem().getOrNull() shouldContainOnly listOf(
                        SuggestedScreenplayIdSample.BreakingBad,
                        SuggestedScreenplayIdSample.Inception,
                        SuggestedScreenplayId(
                            TmdbScreenplayIdSample.Dexter,
                            SuggestionSource.PersonalSuggestions
                        ),
                        SuggestedScreenplayId(
                            TmdbScreenplayIdSample.TheWolfOfWallStreet,
                            SuggestionSource.PersonalSuggestions
                        )
                    )
                }
            }
        }
    }
})

private class RealUpdateSuggestionsTestScenario(
    val sut: RealUpdateSuggestions,
    val suggestionRepository: FakeSuggestionRepository
)

private fun TestScenario(
    generateSuggestionsResult: Either<SuggestionError, Nel<SuggestedScreenplay>>,
    getRecommendationsResult: Either<NetworkError, Nel<TmdbScreenplayId>>
): RealUpdateSuggestionsTestScenario {
    val suggestionRepository = FakeSuggestionRepository()
    return RealUpdateSuggestionsTestScenario(
        sut = RealUpdateSuggestions(
            generateSuggestions = FakeGenerateSuggestions(result = generateSuggestionsResult),
            recommendedScreenplayIdsStore = FakeRecommendedScreenplayIdsStore(
                result = getRecommendationsResult
            ),
            suggestionRepository = suggestionRepository
        ),
        suggestionRepository = suggestionRepository
    )
}
