package cinescout.movies.data

import app.cash.turbine.test
import arrow.core.right
import cinescout.movies.data.store.FakeMovieDetailsStore
import cinescout.movies.data.store.FakeRatedMovieIdsStore
import cinescout.movies.data.store.FakeRatedMoviesStore
import cinescout.movies.data.store.FakeWatchlistMovieIdsStore
import cinescout.movies.data.store.FakeWatchlistMoviesStore
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieIdWithPersonalRating
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.sample.DiscoverMoviesParamsSample
import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.sample.MovieWithDetailsSample
import cinescout.movies.domain.sample.MovieWithPersonalRatingSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import store.test.MockStoreOwner

class RealMovieRepositoryTest : BehaviorSpec({
    coroutineTestScope = true

    Given("no cache") {
        val freshMode = MockStoreOwner.Mode.Fresh

        When("discover movies") {
            val movies = listOf(MovieSample.Inception, MovieSample.TheWolfOfWallStreet)
            val discoverMoviesParams = DiscoverMoviesParamsSample.FromInception
            val scenario = TestScenario(
                remoteDiscoverMovies = movies,
                storeOwnerMode = freshMode
            )

            Then("movies are emitted from remote source") {
                scenario.sut.discoverMovies(discoverMoviesParams).test {
                    awaitItem() shouldBe movies.right()
                    awaitComplete()
                }
            }
        }

        When("get rated movies") {
            val ratedMovies = listOf(
                MovieWithPersonalRatingSample.Inception,
                MovieWithPersonalRatingSample.TheWolfOfWallStreet
            )
            val scenario = TestScenario(
                ratedMovies = ratedMovies,
                storeOwnerMode = freshMode
            )

            Then("movies are emitted") {
                scenario.sut.getAllRatedMovies(refresh = true).test {
                    awaitItem().dataOrNull() shouldBe ratedMovies.right()
                    awaitComplete()
                }
            }
        }

        When("get watchlist movies") {
            val watchlistMovies = listOf(
                MovieSample.Inception,
                MovieSample.TheWolfOfWallStreet
            )
            val scenario = TestScenario(
                watchlistMovies = watchlistMovies,
                storeOwnerMode = freshMode
            )

            Then("movies are emitted from remote source") {
                scenario.sut.getAllWatchlistMovies(refresh = true).test {
                    awaitItem().dataOrNull() shouldBe watchlistMovies.right()
                    awaitComplete()
                }
            }
        }
    }
})

private class RealMovieRepositoryTestScenario(
    val sut: RealMovieRepository
)

private fun TestScenario(
    ratedMovies: List<MovieWithPersonalRating>? = null,
    remoteDiscoverMovies: List<Movie>? = null,
    remoteRatedMovieIds: List<MovieIdWithPersonalRating>? = null,
    remoteWatchlistMovieIds: List<TmdbMovieId>? = null,
    storeOwnerMode: MockStoreOwner.Mode,
    watchlistMovies: List<Movie>? = null
): RealMovieRepositoryTestScenario {
    val remoteMoviesDetails = listOf(
        MovieWithDetailsSample.Inception,
        MovieWithDetailsSample.TheWolfOfWallStreet,
        MovieWithDetailsSample.War
    )
    return RealMovieRepositoryTestScenario(
        sut = RealMovieRepository(
            localMovieDataSource = FakeLocalMovieDataSource(),
            movieDetailsStore = FakeMovieDetailsStore(moviesDetails = remoteMoviesDetails),
            ratedMovieIdsStore = FakeRatedMovieIdsStore(ratedMovies = ratedMovies),
            ratedMoviesStore = FakeRatedMoviesStore(ratedMovies = ratedMovies),
            remoteMovieDataSource = FakeRemoteMovieDataSource(
                discoverMovies = remoteDiscoverMovies,
                moviesDetails = remoteMoviesDetails,
                ratedMovieIds = remoteRatedMovieIds,
                watchlistMovieIds = remoteWatchlistMovieIds
            ),
            storeOwner = MockStoreOwner(mode = storeOwnerMode),
            watchlistMovieIdsStore = FakeWatchlistMovieIdsStore(movies = watchlistMovies),
            watchlistMoviesStore = FakeWatchlistMoviesStore(movies = watchlistMovies)
        )
    )
}
