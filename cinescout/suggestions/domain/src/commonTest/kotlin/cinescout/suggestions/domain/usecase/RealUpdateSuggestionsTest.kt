package cinescout.suggestions.domain.usecase

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.Nel
import arrow.core.left
import arrow.core.nonEmptyListOf
import arrow.core.right
import cinescout.error.DataError
import cinescout.error.NetworkError
import cinescout.movies.domain.sample.TmdbMovieIdSample
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.repository.FakeScreenplayRepository
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import cinescout.suggestions.domain.FakeSuggestionRepository
import cinescout.suggestions.domain.model.SuggestedMovie
import cinescout.suggestions.domain.model.SuggestedMovieId
import cinescout.suggestions.domain.model.SuggestedTvShow
import cinescout.suggestions.domain.model.SuggestedTvShowId
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.model.SuggestionSource
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.sample.SuggestedMovieIdSample
import cinescout.suggestions.domain.sample.SuggestedMovieSample
import cinescout.suggestions.domain.sample.SuggestedTvShowIdSample
import cinescout.suggestions.domain.sample.SuggestedTvShowSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.shouldBe

class RealUpdateSuggestionsTest : BehaviorSpec({

    Given("updating suggestions") {
        val suggestedMovies = nonEmptyListOf(SuggestedMovieSample.Inception)
        val suggestedTvShows = nonEmptyListOf(SuggestedTvShowSample.BreakingBad)
        val recommendations = nonEmptyListOf(TmdbScreenplayIdSample.Dexter, TmdbScreenplayIdSample.TheWolfOfWallStreet)

        val dataError = DataError.Remote(NetworkError.Unknown)
        When("generate movie suggestions is error") {
            val error = SuggestionError.Source(dataError).left()
            val scenario = TestScenario(
                generateSuggestedMoviesResult = error,
                generateSuggestedTvShowsResult = suggestedTvShows.right(),
                getRecommendationsResult = recommendations.right()
            )

            Then("error is emitted") {
                scenario.sut(SuggestionsMode.Quick) shouldBe dataError.left()
            }
        }

        When("generate tv show suggestions is error") {
            val error = SuggestionError.Source(dataError).left()
            val scenario = TestScenario(
                generateSuggestedMoviesResult = suggestedMovies.right(),
                generateSuggestedTvShowsResult = error,
                getRecommendationsResult = recommendations.right()
            )

            Then("error is emitted") {
                scenario.sut(SuggestionsMode.Quick) shouldBe dataError.left()
            }
        }

        When("get recommended is error") {
            val scenario = TestScenario(
                generateSuggestedMoviesResult = suggestedMovies.right(),
                generateSuggestedTvShowsResult = suggestedTvShows.right(),
                getRecommendationsResult = dataError.left()
            )

            Then("error is emitted") {
                scenario.sut(SuggestionsMode.Quick) shouldBe dataError.left()
            }
        }

        When("all are success") {
            val scenario = TestScenario(
                generateSuggestedMoviesResult = suggestedMovies.right(),
                generateSuggestedTvShowsResult = suggestedTvShows.right(),
                getRecommendationsResult = recommendations.right()
            )

            val result = scenario.sut(SuggestionsMode.Quick)

            Then("unit is returned") {
                result shouldBe Unit.right()
            }

            Then("suggested and recommended movies are stored") {
                scenario.suggestionRepository.getSuggestedMovieIds().test {
                    awaitItem().getOrNull() shouldContainOnly listOf(
                        SuggestedMovieIdSample.Inception,
                        SuggestedMovieId(TmdbMovieIdSample.TheWolfOfWallStreet, SuggestionSource.PersonalSuggestions)
                    )
                }
            }

            Then("suggested and recommended tv shows are stored") {
                scenario.suggestionRepository.getSuggestedTvShowIds().test {
                    awaitItem().getOrNull() shouldContainOnly listOf(
                        SuggestedTvShowIdSample.BreakingBad,
                        SuggestedTvShowId(TmdbScreenplayIdSample.Dexter, SuggestionSource.PersonalSuggestions)
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
    generateSuggestedMoviesResult: Either<SuggestionError, Nel<SuggestedMovie>>,
    generateSuggestedTvShowsResult: Either<SuggestionError, Nel<SuggestedTvShow>>,
    getRecommendationsResult: Either<DataError, Nel<TmdbScreenplayId>>
): RealUpdateSuggestionsTestScenario {
    val suggestionRepository = FakeSuggestionRepository()
    return RealUpdateSuggestionsTestScenario(
        sut = RealUpdateSuggestions(
            generateSuggestedMovies = FakeGenerateSuggestedMovies(result = generateSuggestedMoviesResult),
            generateSuggestedTvShows = FakeGenerateSuggestedTvShows(result = generateSuggestedTvShowsResult),
            screenplayRepository = FakeScreenplayRepository(recommendedIdsResult = getRecommendationsResult),
            suggestionRepository = suggestionRepository
        ),
        suggestionRepository = suggestionRepository
    )
}
