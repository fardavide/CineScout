package cinescout.movies.data

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieIdWithPersonalRating
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.sample.DiscoverMoviesParamsSample
import cinescout.movies.domain.sample.MovieIdWithPersonalRatingSample
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

        When("get personal recommendations ids") {
            val recommendedMovies = listOf(
                TmdbMovieIdSample.Inception,
                TmdbMovieIdSample.TheWolfOfWallStreet
            )

            And("refresh is never") {
                val scenario = TestScenario(
                    remotePersonalRecommendationIds = recommendedMovies,
                    storeOwnerMode = freshMode
                )

                Then("no cache is emitted") {
                    scenario.sut.getPersonalRecommendationIds(Refresh.Never).test {
                        awaitItem() shouldBe DataError.Local.NoCache.left()
                    }
                }
            }

            And("refresh is once") {
                val scenario = TestScenario(
                    remotePersonalRecommendationIds = recommendedMovies,
                    storeOwnerMode = freshMode
                )

                Then("movies are emitted from remote source") {
                    scenario.sut.getPersonalRecommendationIds(Refresh.Once).test {
                        awaitItem() shouldBe recommendedMovies.right()
                    }
                }
            }

            And("refresh is if needed") {
                val scenario = TestScenario(
                    remotePersonalRecommendationIds = recommendedMovies,
                    storeOwnerMode = freshMode
                )

                Then("movies are emitted from remote source") {
                    scenario.sut.getPersonalRecommendationIds(Refresh.IfNeeded).test {
                        awaitItem() shouldBe recommendedMovies.right()
                    }
                }
            }

            And("refresh is if expired") {
                val scenario = TestScenario(
                    remotePersonalRecommendationIds = recommendedMovies,
                    storeOwnerMode = freshMode
                )

                Then("movies are emitted from remote source") {
                    scenario.sut.getPersonalRecommendationIds(Refresh.IfExpired()).test {
                        awaitItem() shouldBe recommendedMovies.right()
                    }
                }
            }

            And("refresh is with interval") {
                val scenario = TestScenario(
                    remotePersonalRecommendationIds = recommendedMovies,
                    storeOwnerMode = freshMode
                )

                Then("movies are emitted from remote source") {
                    scenario.sut.getPersonalRecommendationIds(Refresh.WithInterval()).test {
                        awaitItem() shouldBe recommendedMovies.right()
                    }
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
    remoteDiscoverMovies: List<Movie>? = null,
    remotePersonalRecommendationIds: List<TmdbMovieId>? = null,
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
            remoteMovieDataSource = FakeRemoteMovieDataSource(
                discoverMovies = remoteDiscoverMovies,
                moviesDetails = remoteMoviesDetails,
                personalRecommendationIds = remotePersonalRecommendationIds,
                ratedMovieIds = remoteRatedMovieIds,
                watchlistMovieIds = remoteWatchlistMovieIds
            ),
            storeOwner = MockStoreOwner(mode = storeOwnerMode)
        )
    )
}
