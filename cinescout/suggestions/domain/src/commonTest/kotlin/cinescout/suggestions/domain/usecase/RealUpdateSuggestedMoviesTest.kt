package cinescout.suggestions.domain.usecase

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.Nel
import arrow.core.left
import arrow.core.nonEmptyListOf
import arrow.core.right
import cinescout.suggestions.domain.FakeSuggestionRepository
import cinescout.suggestions.domain.model.SuggestedMovie
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.sample.SuggestedMovieSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class RealUpdateSuggestedMoviesTest : BehaviorSpec({

    Given("updating suggestions") {

        When("generate suggestions is error") {
            val error = SuggestionError.NoSuggestions.left()
            val scenario = TestScenario(generateSuggestionsResult = error)

            Then("error is emitted") {
                scenario.sut(SuggestionsMode.Quick) shouldBe error
            }
        }

        When("generate suggestions is success") {
            val movies = nonEmptyListOf(SuggestedMovieSample.Inception)
            val scenario = TestScenario(generateSuggestionsResult = movies.right())

            val result = scenario.sut(SuggestionsMode.Quick)

            Then("unit is returned") {
                result shouldBe Unit.right()
            }

            Then("suggestions are stored") {
                scenario.suggestionRepository.getSuggestedMovies().test {
                    awaitItem() shouldBe movies.right()
                }
            }
        }
    }
})

private class RealUpdateSuggestedMoviesTestScenario(
    val sut: RealUpdateSuggestedMovies,
    val suggestionRepository: FakeSuggestionRepository
)

private fun TestScenario(
    generateSuggestionsResult: Either<SuggestionError, Nel<SuggestedMovie>>
): RealUpdateSuggestedMoviesTestScenario {
    val suggestionRepository = FakeSuggestionRepository()
    return RealUpdateSuggestedMoviesTestScenario(
        sut = RealUpdateSuggestedMovies(
            generateSuggestedMovies = FakeGenerateSuggestedMovies(result = generateSuggestionsResult),
            suggestionRepository = suggestionRepository
        ),
        suggestionRepository = suggestionRepository
    )
}
