package cinescout.suggestions.domain.usecase

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.Nel
import arrow.core.left
import arrow.core.nonEmptyListOf
import arrow.core.right
import cinescout.suggestions.domain.FakeSuggestionRepository
import cinescout.suggestions.domain.model.SuggestedMovie
import cinescout.suggestions.domain.model.SuggestedTvShow
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.sample.SuggestedMovieIdSample
import cinescout.suggestions.domain.sample.SuggestedMovieSample
import cinescout.suggestions.domain.sample.SuggestedTvShowIdSample
import cinescout.suggestions.domain.sample.SuggestedTvShowSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class RealUpdateSuggestionsTest : BehaviorSpec({

    Given("updating suggestions") {
        val movieIds = nonEmptyListOf(SuggestedMovieIdSample.Inception)
        val movies = nonEmptyListOf(SuggestedMovieSample.Inception)
        val tvShowIds = nonEmptyListOf(SuggestedTvShowIdSample.BreakingBad)
        val tvShows = nonEmptyListOf(SuggestedTvShowSample.BreakingBad)

        When("generate movie suggestions is error") {
            val error = SuggestionError.NoSuggestions.left()
            val scenario = TestScenario(
                generateSuggestedMoviesResult = error,
                generateSuggestedTvShowsResult = tvShows.right()
            )

            Then("error is emitted") {
                scenario.sut(SuggestionsMode.Quick) shouldBe error
            }
        }

        When("generate tv show suggestions is error") {
            val error = SuggestionError.NoSuggestions.left()
            val scenario = TestScenario(
                generateSuggestedMoviesResult = movies.right(),
                generateSuggestedTvShowsResult = error
            )

            Then("error is emitted") {
                scenario.sut(SuggestionsMode.Quick) shouldBe error
            }
        }

        When("generate suggestions is success") {
            val scenario = TestScenario(
                generateSuggestedMoviesResult = movies.right(),
                generateSuggestedTvShowsResult = tvShows.right()
            )

            val result = scenario.sut(SuggestionsMode.Quick)

            Then("unit is returned") {
                result shouldBe Unit.right()
            }

            Then("suggested movies are stored") {
                scenario.suggestionRepository.getSuggestedMovieIds().test {
                    awaitItem() shouldBe movieIds.right()
                }
            }

            Then("suggested tv shows are stored") {
                scenario.suggestionRepository.getSuggestedTvShowIds().test {
                    awaitItem() shouldBe tvShowIds.right()
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
    generateSuggestedMoviesResult: Either<SuggestionError, Nel<SuggestedMovie>>,
    generateSuggestedTvShowsResult: Either<SuggestionError, Nel<SuggestedTvShow>>
): RealUpdateSuggestionsTestScenario {
    val suggestionRepository = FakeSuggestionRepository()
    return RealUpdateSuggestionsTestScenario(
        sut = RealUpdateSuggestions(
            generateSuggestedMovies = FakeGenerateSuggestedMovies(result = generateSuggestedMoviesResult),
            generateSuggestedTvShows = FakeGenerateSuggestedTvShows(result = generateSuggestedTvShowsResult),
            suggestionRepository = suggestionRepository
        ),
        suggestionRepository = suggestionRepository
    )
}
