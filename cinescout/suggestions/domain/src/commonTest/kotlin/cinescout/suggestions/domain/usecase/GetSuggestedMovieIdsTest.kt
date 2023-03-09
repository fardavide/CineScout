package cinescout.suggestions.domain.usecase

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.Nel
import arrow.core.left
import arrow.core.nonEmptyListOf
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.movies.domain.FakeMovieRepository
import cinescout.suggestions.domain.FakeSuggestionRepository
import cinescout.suggestions.domain.model.SuggestedMovieId
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.sample.SuggestedMovieIdSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.measureTime

/*
 * TODO: does not start update while already updating
 *  Waiting for actors in KMP: https://github.com/Kotlin/kotlinx.coroutines/issues/87
 */
class GetSuggestedMovieIdsTest : BehaviorSpec({
    coroutineTestScope = true

    Given("stored suggestions are above the minimum") {
        val suggestions = nonEmptyListOf(
            SuggestedMovieIdSample.Inception,
            SuggestedMovieIdSample.TheWolfOfWallStreet,
            SuggestedMovieIdSample.War
        )

        When("getting suggestions") {
            val scenario = TestScenario(suggestions = suggestions)

            Then("it should return the stored suggestions") {
                scenario.sut().test {
                    awaitItem() shouldBe suggestions.right()
                }
            }

            Then("it should not update the suggestions") {
                scenario.updateSuggestions.invoked shouldBe false
            }
        }
    }

    Given("stored suggestions are below the minimum") {
        val suggestions = nonEmptyListOf(
            SuggestedMovieIdSample.Inception,
            SuggestedMovieIdSample.TheWolfOfWallStreet
        )

        When("getting suggestions") {
            val scenario = TestScenario(suggestions = suggestions)

            Then("it should return the stored suggestions") {
                scenario.sut().test {
                    awaitItem() shouldBe suggestions.right()
                }
            }

            Then("it should update the suggestions") {
                scenario.updateSuggestions.invoked shouldBe true
            }
        }

        When("updating suggestions") {
            val updatingSuggestionsDelay = 10.seconds
            val suggestionsFlow: MutableStateFlow<Either<SuggestionError, Nel<SuggestedMovieId>>> =
                MutableStateFlow(suggestions.right())
            val newSuggestions = nonEmptyListOf(
                SuggestedMovieIdSample.Inception,
                SuggestedMovieIdSample.TheWolfOfWallStreet,
                SuggestedMovieIdSample.War
            )
            val scenario = TestScenario(
                suggestionsFlow = suggestionsFlow,
                updatingSuggestionsDelay = updatingSuggestionsDelay
            )

            scenario.sut().test {

                val time = testCoroutineScheduler.timeSource.measureTime {
                    Then("it should return the stored suggestions") {
                        awaitItem() shouldBe suggestions.right()
                        suggestionsFlow.emit(newSuggestions.right())
                        awaitItem() shouldBe newSuggestions.right()
                    }
                }

                And("emission isn't blocked by the update") {
                    time shouldBeLessThan updatingSuggestionsDelay
                }
            }
        }
    }

    Given("some stored suggestions") {
        val suggestions = nonEmptyListOf(
            SuggestedMovieIdSample.Inception
        )
        val suggestionsFlow: MutableStateFlow<Either<SuggestionError, Nel<SuggestedMovieId>>> =
            MutableStateFlow(suggestions.right())

        When("suggestions are consumed") {
            val scenario = TestScenario(suggestionsFlow = suggestionsFlow)
            scenario.sut().test {
                awaitItem() shouldBe suggestions.right()

                And("new suggestions are available") {
                    val newSuggestions = nonEmptyListOf(
                        SuggestedMovieIdSample.TheWolfOfWallStreet
                    )
                    suggestionsFlow.emit(newSuggestions.right())

                    Then("new suggestions are emitted") {
                        awaitItem() shouldBe newSuggestions.right()
                    }
                }
            }
        }
    }

    Given("stored suggestions are empty") {
        val suggestions: Nel<SuggestedMovieId>? = null

        When("error updating suggestions") {
            val updatingSuggestionsError = SuggestionError.Source(NetworkError.Forbidden)
            val scenario = TestScenario(
                suggestions = suggestions,
                updatingSuggestionsError = updatingSuggestionsError
            )

            Then("it emits the error") {
                scenario.sut().test {
                    awaitItem() shouldBe updatingSuggestionsError.left()
                }
            }

            Then("it should try to update the suggestions") {
                scenario.updateSuggestions.invoked shouldBe true
            }
        }
    }
})

private const val TestMinimumSuggestions = 3

private class GetSuggestedMovieIdsTestScenario(
    val sut: GetSuggestedMovieIds,
    val updateSuggestions: FakeUpdateSuggestions
)

private fun TestScenario(
    suggestions: Nel<SuggestedMovieId>? = null,
    suggestionsFlow: MutableStateFlow<Either<SuggestionError, Nel<SuggestedMovieId>>> =
        MutableStateFlow(suggestions?.right() ?: SuggestionError.NoSuggestions.left()),
    updatingSuggestionsDelay: Duration = Duration.ZERO,
    updatingSuggestionsError: SuggestionError? = null
): GetSuggestedMovieIdsTestScenario {
    val updateSuggestions = FakeUpdateSuggestions(
        delay = updatingSuggestionsDelay,
        error = updatingSuggestionsError
    )
    return GetSuggestedMovieIdsTestScenario(
        sut = GetSuggestedMovieIds(
            movieRepository = FakeMovieRepository(),
            suggestionRepository = FakeSuggestionRepository(suggestedMovieIdsFlow = suggestionsFlow),
            updateSuggestions = updateSuggestions,
            updateIfSuggestionsLessThan = TestMinimumSuggestions
        ),
        updateSuggestions = updateSuggestions
    )
}
