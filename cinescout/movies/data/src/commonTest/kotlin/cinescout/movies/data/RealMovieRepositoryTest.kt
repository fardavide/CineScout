package cinescout.movies.data

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieIdWithPersonalRating
import cinescout.movies.domain.sample.DiscoverMoviesParamsSample
import cinescout.movies.domain.sample.MovieIdWithPersonalRatingSample
import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.sample.MovieWithDetailsSample
import cinescout.movies.domain.sample.MovieWithPersonalRatingSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import store.Refresh
import store.test.MockStoreOwner

class RealMovieRepositoryTest : BehaviorSpec({

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
            val movieIds = listOf(
                MovieIdWithPersonalRatingSample.Inception,
                MovieIdWithPersonalRatingSample.TheWolfOfWallStreet
            )
            val ratedMovies = listOf(
                MovieWithPersonalRatingSample.Inception,
                MovieWithPersonalRatingSample.TheWolfOfWallStreet
            )

            And("refresh is never") {
                val scenario = TestScenario(
                    remoteRatedMovieIds = movieIds,
                    storeOwnerMode = freshMode
                )

                Then("no cache is emitted") {
                    scenario.sut.getAllRatedMovies(Refresh.Never).test {
                        awaitItem() shouldBe DataError.Local.NoCache.left()
                    }
                }
            }

            And("refresh is once") {
                val scenario = TestScenario(
                    remoteRatedMovieIds = movieIds,
                    storeOwnerMode = freshMode
                )

                Then("movies are emitted from remote source") {
                    scenario.sut.getAllRatedMovies(Refresh.Once).test {
                        awaitItem() shouldBe ratedMovies.right()
                    }
                }
            }

            And("refresh is if needed") {
                val scenario = TestScenario(
                    remoteRatedMovieIds = movieIds,
                    storeOwnerMode = freshMode
                )

                Then("movies are emitted from remote source") {
                    scenario.sut.getAllRatedMovies(Refresh.IfNeeded).test {
                        awaitItem() shouldBe ratedMovies.right()
                    }
                }
            }

            And("refresh is if expired") {
                val scenario = TestScenario(
                    remoteRatedMovieIds = movieIds,
                    storeOwnerMode = freshMode
                )

                Then("movies are emitted from remote source") {
                    scenario.sut.getAllRatedMovies(Refresh.IfExpired()).test {
                        awaitItem() shouldBe ratedMovies.right()
                    }
                }
            }

            And("refresh is with interval") {
                val scenario = TestScenario(
                    remoteRatedMovieIds = movieIds,
                    storeOwnerMode = freshMode
                )

                Then("movies are emitted from remote source") {
                    scenario.sut.getAllRatedMovies(Refresh.WithInterval()).test {
                        awaitItem() shouldBe ratedMovies.right()
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
    remoteDiscoverMovies: List<Movie>? = null,
    remoteRatedMovieIds: List<MovieIdWithPersonalRating>? = null,
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
            remoteMovieDataSource = FakeRemoteMovieDataSource(
                discoverMovies = remoteDiscoverMovies,
                moviesDetails = remoteMoviesDetails,
                ratedMovieIds = remoteRatedMovieIds
            ),
            storeOwner = MockStoreOwner(mode = storeOwnerMode)
        )
    )
}
