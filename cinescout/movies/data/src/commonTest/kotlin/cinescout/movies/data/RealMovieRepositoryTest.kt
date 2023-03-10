package cinescout.movies.data

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import cinescout.movies.data.store.FakeMovieDetailsStore
import cinescout.movies.data.store.FakeRatedMovieIdsStore
import cinescout.movies.data.store.FakeRatedMoviesStore
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieIdWithPersonalRating
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.sample.DiscoverMoviesParamsSample
import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.sample.MovieWithDetailsSample
import cinescout.movies.domain.sample.MovieWithPersonalRatingSample
import cinescout.movies.domain.sample.TmdbMovieIdSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import store.Refresh
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
            val movieIds = listOf(
                TmdbMovieIdSample.Inception,
                TmdbMovieIdSample.TheWolfOfWallStreet
            )
            val watchlistMovies = listOf(
                MovieSample.Inception,
                MovieSample.TheWolfOfWallStreet
            )

            And("refresh is never") {
                val scenario = TestScenario(
                    remoteWatchlistMovieIds = movieIds,
                    storeOwnerMode = freshMode
                )

                Then("no cache is emitted") {
                    scenario.sut.getAllWatchlistMovies(Refresh.Never).test {
                        awaitItem() shouldBe DataError.Local.NoCache.left()
                    }
                }
            }

            And("refresh is once") {
                val scenario = TestScenario(
                    remoteWatchlistMovieIds = movieIds,
                    storeOwnerMode = freshMode
                )

                Then("movies are emitted from remote source") {
                    scenario.sut.getAllWatchlistMovies(Refresh.Once).test {
                        awaitItem() shouldBe watchlistMovies.right()
                    }
                }
            }

            And("refresh is if needed") {
                val scenario = TestScenario(
                    remoteWatchlistMovieIds = movieIds,
                    storeOwnerMode = freshMode
                )

                Then("movies are emitted from remote source") {
                    scenario.sut.getAllWatchlistMovies(Refresh.IfNeeded).test {
                        awaitItem() shouldBe watchlistMovies.right()
                    }
                }
            }

            And("refresh is if expired") {
                val scenario = TestScenario(
                    remoteWatchlistMovieIds = movieIds,
                    storeOwnerMode = freshMode
                )

                Then("movies are emitted from remote source") {
                    scenario.sut.getAllWatchlistMovies(Refresh.IfExpired()).test {
                        awaitItem() shouldBe watchlistMovies.right()
                    }
                }
            }

            And("refresh is with interval") {
                val scenario = TestScenario(
                    remoteWatchlistMovieIds = movieIds,
                    storeOwnerMode = freshMode
                )

                Then("movies are emitted from remote source") {
                    scenario.sut.getAllWatchlistMovies(Refresh.WithInterval()).test {
                        awaitItem() shouldBe watchlistMovies.right()
                    }
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
    storeOwnerMode: MockStoreOwner.Mode
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
            storeOwner = MockStoreOwner(mode = storeOwnerMode)
        )
    )
}
