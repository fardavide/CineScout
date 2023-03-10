package cinescout.suggestions.domain.usecase

import cinescout.movies.domain.FakeMovieRepository
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.usecase.FakeGetAllDislikedMovies
import cinescout.movies.domain.usecase.FakeGetAllLikedMovies
import cinescout.movies.domain.usecase.FakeGetAllRatedMovies
import cinescout.movies.domain.usecase.FakeGetAllWatchlistMovies
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import cinescout.suggestions.domain.model.SuggestionsMode
import io.kotest.core.spec.style.BehaviorSpec

class RealGenerateSuggestedMoviesTest : BehaviorSpec({

    Given("quick mode") {
        val suggestionsMode = SuggestionsMode.Quick

        When("when generate suggested movies") {
            val ratedMoviesFlow = storeFlowOf(emptyList<MovieWithPersonalRating>())
            val watchlistMoviesFlow = storeFlowOf(emptyList<Movie>())
            val scenario = TestScenario(
                ratedMoviesFlow = ratedMoviesFlow,
                watchlistMoviesFlow = watchlistMoviesFlow
            )
            scenario.sut(suggestionsMode)

            // TODO: Then
        }
    }

    Given("deep mode") {
        val suggestionsMode = SuggestionsMode.Deep

        When("when generate suggested movies") {
            val ratedMoviesFlow = storeFlowOf(emptyList<MovieWithPersonalRating>())
            val watchlistMoviesFlow = storeFlowOf(emptyList<Movie>())
            val scenario = TestScenario(
                ratedMoviesFlow = ratedMoviesFlow,
                watchlistMoviesFlow = watchlistMoviesFlow
            )
            scenario.sut(suggestionsMode)

            // TODO: Then
        }
    }
})

private class RealGenerateSuggestedMoviesTestScenario(
    val sut: GenerateSuggestedMovies
)

private fun TestScenario(
    ratedMoviesFlow: StoreFlow<List<MovieWithPersonalRating>> = storeFlowOf(emptyList()),
    watchlistMoviesFlow: StoreFlow<List<Movie>> = storeFlowOf(emptyList())
): RealGenerateSuggestedMoviesTestScenario {
    return RealGenerateSuggestedMoviesTestScenario(
        sut = RealGenerateSuggestedMovies(
            getAllDislikedMovies = FakeGetAllDislikedMovies(),
            getAllLikedMovies = FakeGetAllLikedMovies(),
            getAllRatedMovies = FakeGetAllRatedMovies(store = ratedMoviesFlow),
            getAllWatchlistMovies = FakeGetAllWatchlistMovies(store = watchlistMoviesFlow),
            movieRepository = FakeMovieRepository()
        )
    )
}
