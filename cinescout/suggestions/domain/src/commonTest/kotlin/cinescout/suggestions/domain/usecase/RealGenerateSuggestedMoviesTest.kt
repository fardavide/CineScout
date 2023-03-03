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
import io.kotest.matchers.shouldBe
import store.PagedStore
import store.Paging
import store.test.emptyFakePagedStore

class RealGenerateSuggestedMoviesTest : BehaviorSpec({

    Given("quick mode") {
        val suggestionsMode = SuggestionsMode.Quick

        When("when generate suggested movies") {
            val ratedMoviesPagedStore = emptyFakePagedStore<MovieWithPersonalRating>()
            val watchlistMoviesPagedStore = emptyFakePagedStore<Movie>()
            val scenario = TestScenario(
                ratedMoviesPagedStore = ratedMoviesPagedStore,
                watchlistMoviesPagedStore = watchlistMoviesPagedStore
            )
            scenario.sut(suggestionsMode)

            Then("fetches only the first page of rated movies") {
                ratedMoviesPagedStore.didInvokeLoadAll shouldBe false
                ratedMoviesPagedStore.loadMoreInvocationCount shouldBe 0
            }

            Then("fetches only the first page of watchlist movies") {
                watchlistMoviesPagedStore.didInvokeLoadAll shouldBe false
                watchlistMoviesPagedStore.loadMoreInvocationCount shouldBe 0
            }
        }
    }

    Given("deep mode") {
        val suggestionsMode = SuggestionsMode.Deep

        When("when generate suggested movies") {
            val ratedMoviesPagedStore = emptyFakePagedStore<MovieWithPersonalRating>()
            val watchlistMoviesPagedStore = emptyFakePagedStore<Movie>()
            val scenario = TestScenario(
                ratedMoviesPagedStore = ratedMoviesPagedStore,
                watchlistMoviesPagedStore = watchlistMoviesPagedStore
            )
            scenario.sut(suggestionsMode)

            Then("fetches all the pages of rated movies") {
                ratedMoviesPagedStore.didInvokeLoadAll shouldBe true
                ratedMoviesPagedStore.loadMoreInvocationCount shouldBe 0
            }

            Then("fetches all the pages of watchlist movies") {
                watchlistMoviesPagedStore.didInvokeLoadAll shouldBe true
                watchlistMoviesPagedStore.loadMoreInvocationCount shouldBe 0
            }
        }
    }
})

private class RealGenerateSuggestedMoviesTestScenario(
    val sut: GenerateSuggestedMovies
)

private fun TestScenario(
    ratedMoviesPagedStore: PagedStore<MovieWithPersonalRating, Paging> = emptyFakePagedStore(),
    watchlistMoviesPagedStore: PagedStore<Movie, Paging> = emptyFakePagedStore()
): RealGenerateSuggestedMoviesTestScenario {
    return RealGenerateSuggestedMoviesTestScenario(
        sut = RealGenerateSuggestedMovies(
            getAllDislikedMovies = FakeGetAllDislikedMovies(),
            getAllLikedMovies = FakeGetAllLikedMovies(),
            getAllRatedMovies = FakeGetAllRatedMovies(pagedStore = ratedMoviesPagedStore),
            getAllWatchlistMovies = FakeGetAllWatchlistMovies(pagedStore = watchlistMoviesPagedStore),
            movieRepository = FakeMovieRepository()
        )
    )
}
