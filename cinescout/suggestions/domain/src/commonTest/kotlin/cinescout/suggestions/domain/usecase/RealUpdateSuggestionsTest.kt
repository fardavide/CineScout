package cinescout.suggestions.domain.usecase

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.Nel
import arrow.core.left
import arrow.core.nonEmptyListOf
import arrow.core.right
import cinescout.anticipated.domain.store.FakeMostAnticipatedIdsStore
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.sample.ScreenplayIdsSample
import cinescout.screenplay.domain.store.FakeRecommendedScreenplayIdsStore
import cinescout.store5.Store5ReadResponse
import cinescout.suggestions.domain.model.SuggestedScreenplay
import cinescout.suggestions.domain.model.SuggestedScreenplayId
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.model.SuggestionSource
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.repository.FakeSuggestionRepository
import cinescout.suggestions.domain.sample.SuggestedScreenplayIdSample
import cinescout.suggestions.domain.sample.SuggestedScreenplaySample
import cinescout.trending.domain.store.FakeTrendingIdsStore
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.mobilenativefoundation.store.store5.StoreReadResponseOrigin

class RealUpdateSuggestionsTest : BehaviorSpec({

    Given("updating suggestions") {
        val anticipated = listOf(ScreenplayIdsSample.Avatar3)
        val suggestions = nonEmptyListOf(SuggestedScreenplaySample.BreakingBad, SuggestedScreenplaySample.Inception)
        val recommendations = nonEmptyListOf(ScreenplayIdsSample.Dexter, ScreenplayIdsSample.TheWolfOfWallStreet)
        val trending = listOf(ScreenplayIdsSample.Grimm)

        val networkError = NetworkError.Unknown
        When("generate suggestions is error") {
            val scenario = TestScenario(
                generateSuggestionsResult = SuggestionError.Source(networkError).left(),
                getAnticipatedFetchResult = anticipated.right(),
                getRecommendationsFetchResult = recommendations.right(),
                getTrendingFetchResult = trending.right()
            )

            Then("error is emitted") {
                scenario.sut(ScreenplayType.All, SuggestionsMode.Quick) shouldBe networkError.left()
            }
        }

        When("get most anticipated is error") {
            val scenario = TestScenario(
                generateSuggestionsResult = suggestions.right(),
                getAnticipatedFetchResult = networkError.left()
            )

            Then("error is emitted") {
                scenario.sut(ScreenplayType.All, SuggestionsMode.Quick) shouldBe networkError.left()
            }
        }

        When("get recommended is skipped") {
            val scenario = TestScenario(
                generateSuggestionsResult = suggestions.right(),
                getAnticipatedFetchResult = anticipated.right(),
                getRecommendationsResponse = Store5ReadResponse.Skipped,
                getTrendingFetchResult = trending.right()
            )

            Then("success is emitted") {
                scenario.sut(ScreenplayType.All, SuggestionsMode.Quick) shouldBe Unit.right()
            }
        }

        When("get recommended is error") {
            val scenario = TestScenario(
                generateSuggestionsResult = suggestions.right(),
                getAnticipatedFetchResult = anticipated.right(),
                getRecommendationsFetchResult = networkError.left(),
                getTrendingFetchResult = trending.right()
            )

            Then("error is emitted") {
                scenario.sut(ScreenplayType.All, SuggestionsMode.Quick) shouldBe networkError.left()
            }
        }

        When("get trending is error") {
            val scenario = TestScenario(
                generateSuggestionsResult = suggestions.right(),
                getAnticipatedFetchResult = anticipated.right(),
                getRecommendationsFetchResult = recommendations.right(),
                getTrendingFetchResult = networkError.left()
            )

            Then("error is emitted") {
                scenario.sut(ScreenplayType.All, SuggestionsMode.Quick) shouldBe networkError.left()
            }
        }

        When("all are success") {
            val scenario = TestScenario(
                generateSuggestionsResult = suggestions.right(),
                getAnticipatedFetchResult = anticipated.right(),
                getRecommendationsFetchResult = recommendations.right(),
                getTrendingFetchResult = trending.right()
            )

            val result = scenario.sut(ScreenplayType.All, SuggestionsMode.Quick)

            Then("unit is returned") {
                result shouldBe Unit.right()
            }

            Then("suggested, anticipated, recommended and trending are stored") {
                scenario.suggestionRepository.getSuggestionIds(ScreenplayType.All).test {
                    val item = requireNotNull(awaitItem().getOrNull())
                    item shouldHaveSize 6
                    item shouldContainOnly listOf(
                        SuggestedScreenplayId(
                            ScreenplayIdsSample.Avatar3,
                            SuggestionSource.Anticipated
                        ),
                        SuggestedScreenplayIdSample.BreakingBad,
                        SuggestedScreenplayId(
                            ScreenplayIdsSample.Dexter,
                            SuggestionSource.PersonalSuggestions
                        ),
                        SuggestedScreenplayId(
                            ScreenplayIdsSample.Grimm,
                            SuggestionSource.Trending
                        ),
                        SuggestedScreenplayIdSample.Inception,
                        SuggestedScreenplayId(
                            ScreenplayIdsSample.TheWolfOfWallStreet,
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
    getAnticipatedFetchResult: Either<NetworkError, List<ScreenplayIds>> = NetworkError.NotFound.left(),
    getAnticipatedResponse: Store5ReadResponse<List<ScreenplayIds>> = Store5ReadResponse.Data(
        getAnticipatedFetchResult,
        StoreReadResponseOrigin.Fetcher
    ),
    getRecommendationsFetchResult: Either<NetworkError, Nel<ScreenplayIds>> = NetworkError.NotFound.left(),
    getRecommendationsResponse: Store5ReadResponse<Nel<ScreenplayIds>> = Store5ReadResponse.Data(
        getRecommendationsFetchResult,
        StoreReadResponseOrigin.Fetcher
    ),
    getTrendingFetchResult: Either<NetworkError, List<ScreenplayIds>> = NetworkError.NotFound.left(),
    getTrendingResponse: Store5ReadResponse<List<ScreenplayIds>> = Store5ReadResponse.Data(
        getTrendingFetchResult,
        StoreReadResponseOrigin.Fetcher
    )
): RealUpdateSuggestionsTestScenario {
    val suggestionRepository = FakeSuggestionRepository()
    return RealUpdateSuggestionsTestScenario(
        sut = RealUpdateSuggestions(
            anticipatedIdsStore = FakeMostAnticipatedIdsStore(response = getAnticipatedResponse),
            generateSuggestions = FakeGenerateSuggestions(result = generateSuggestionsResult),
            recommendedScreenplayIdsStore = FakeRecommendedScreenplayIdsStore(response = getRecommendationsResponse),
            suggestionRepository = suggestionRepository,
            trendingIdsStore = FakeTrendingIdsStore(response = getTrendingResponse)
        ),
        suggestionRepository = suggestionRepository
    )
}
