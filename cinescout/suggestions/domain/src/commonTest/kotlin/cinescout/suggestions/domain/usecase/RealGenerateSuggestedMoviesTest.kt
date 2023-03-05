package cinescout.suggestions.domain.usecase

import cinescout.movies.domain.FakeMovieRepository
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.usecase.FakeGetAllDislikedMovies
import cinescout.movies.domain.usecase.FakeGetAllLikedMovies
import cinescout.movies.domain.usecase.FakeGetAllRatedMovies
import cinescout.movies.domain.usecase.FakeGetAllWatchlistMovies
import cinescout.suggestions.domain.model.SuggestionsMode
import io.kotest.core.spec.style.BehaviorSpec
import store.Store
import store.builder.emptyListStore

class RealGenerateSuggestedMoviesTest : BehaviorSpec({

    Given("quick mode") {
        val suggestionsMode = SuggestionsMode.Quick

        When("when generate suggested movies") {
            val ratedMoviesPagedStore = emptyListStore<MovieWithPersonalRating>()
            val watchlistMoviesPagedStore = emptyListStore<Movie>()
            val scenario = TestScenario(
                ratedMoviesPagedStore = ratedMoviesPagedStore,
                watchlistMoviesPagedStore = watchlistMoviesPagedStore
            )
            scenario.sut(suggestionsMode)

            // TODO: Then
        }
    }

    Given("deep mode") {
        val suggestionsMode = SuggestionsMode.Deep

        When("when generate suggested movies") {
            val ratedMoviesPagedStore = emptyListStore<MovieWithPersonalRating>()
            val watchlistMoviesPagedStore = emptyListStore<Movie>()
            val scenario = TestScenario(
                ratedMoviesPagedStore = ratedMoviesPagedStore,
                watchlistMoviesPagedStore = watchlistMoviesPagedStore
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
    ratedMoviesPagedStore: Store<List<MovieWithPersonalRating>> = emptyListStore(),
    watchlistMoviesPagedStore: Store<List<Movie>> = emptyListStore()
): RealGenerateSuggestedMoviesTestScenario {
    return RealGenerateSuggestedMoviesTestScenario(
        sut = RealGenerateSuggestedMovies(
            getAllDislikedMovies = FakeGetAllDislikedMovies(),
            getAllLikedMovies = FakeGetAllLikedMovies(),
            getAllRatedMovies = FakeGetAllRatedMovies(store = ratedMoviesPagedStore),
            getAllWatchlistMovies = FakeGetAllWatchlistMovies(store = watchlistMoviesPagedStore),
            movieRepository = FakeMovieRepository()
        )
    )
}
