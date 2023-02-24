package tests

import app.cash.turbine.test
import arrow.core.nonEmptyListOf
import arrow.core.right
import cinescout.common.model.Rating
import cinescout.movies.data.remote.tmdb.testutil.TmdbMovieDetailsJson
import cinescout.movies.data.remote.tmdb.testutil.addMovieDetailsHandler
import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.sample.MovieWithDetailsSample
import cinescout.movies.domain.sample.MovieWithPersonalRatingSample
import cinescout.movies.domain.sample.TmdbMovieIdSample
import cinescout.movies.domain.usecase.GetAllRatedMovies
import cinescout.movies.domain.usecase.GetAllWatchlistMovies
import cinescout.movies.domain.usecase.GetMovieDetails
import cinescout.movies.domain.usecase.RateMovie
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.usecase.GenerateSuggestedMovies
import cinescout.test.mock.junit5.MockAppExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.koin.test.inject
import util.AuthHelper
import util.awaitRemoteData

class MoviesTest : BehaviorSpec({
    val mockAppExtension = MockAppExtension {
        appScope(TestScope(UnconfinedTestDispatcher()))
    }
    extensions(mockAppExtension)

    val authHelper = AuthHelper()

    Given("linked to Tmdb") {
        authHelper.givenLinkedToTmdb()

        When("get movie details") {
            val getMovieDetails: GetMovieDetails by mockAppExtension.inject()

            Then("movie is emitted") {
                getMovieDetails(TmdbMovieIdSample.TheWolfOfWallStreet).test {
                    awaitItem() shouldBe MovieWithDetailsSample.TheWolfOfWallStreet.right()
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        When("get all rated movies") {
            val getAllRatedMovies: GetAllRatedMovies by mockAppExtension.inject()

            Then("rated movies are emitted") {
                getAllRatedMovies().test {
                    awaitRemoteData() shouldBe listOf(MovieWithPersonalRatingSample.Inception)
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        When("get all watchlist movies") {
            val getAllWatchlistMovies: GetAllWatchlistMovies by mockAppExtension.inject()

            Then("watchlist movies are emitted") {
                getAllWatchlistMovies().test {
                    awaitRemoteData() shouldBe listOf(MovieSample.Inception)
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        When("generate quick suggested movies") {
            val generateSuggestedMovies: GenerateSuggestedMovies by mockAppExtension.inject()

            And("movie has empty genres") {
                mockAppExtension.tmdbMockEngine.addMovieDetailsHandler(
                    TmdbMovieIdSample.Inception,
                    TmdbMovieDetailsJson.InceptionWithEmptyGenres
                )

                Then("suggested movies are emitted") {
                    generateSuggestedMovies(SuggestionsMode.Quick).test {
                        awaitItem() shouldBe nonEmptyListOf(MovieSample.TheWolfOfWallStreet).right()
                        cancelAndIgnoreRemainingEvents()
                    }
                }
            }

            And("movie has no genres field") {
                mockAppExtension.tmdbMockEngine.addMovieDetailsHandler(
                    TmdbMovieIdSample.Inception,
                    TmdbMovieDetailsJson.InceptionWithoutGenres
                )

                Then("suggested movies are emitted") {
                    generateSuggestedMovies(SuggestionsMode.Quick).test {
                        awaitItem() shouldBe nonEmptyListOf(MovieSample.TheWolfOfWallStreet).right()
                        cancelAndIgnoreRemainingEvents()
                    }
                }
            }

            And("movie has no release date") {
                mockAppExtension.tmdbMockEngine.addMovieDetailsHandler(
                    TmdbMovieIdSample.Inception,
                    TmdbMovieDetailsJson.InceptionWithoutReleaseDate
                )

                Then("suggested movies are emitted") {
                    generateSuggestedMovies(SuggestionsMode.Quick).test {
                        awaitItem() shouldBe nonEmptyListOf(MovieSample.TheWolfOfWallStreet).right()
                        cancelAndIgnoreRemainingEvents()
                    }
                }
            }

            Then("suggested movies are emitted") {
                generateSuggestedMovies(SuggestionsMode.Quick).test {
                    awaitItem() shouldBe nonEmptyListOf(MovieSample.TheWolfOfWallStreet).right()
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        When("rate movie") {
            val rateMovie: RateMovie by mockAppExtension.inject()

            Then("success") {
                Rating.of(8).tap { rating ->
                    rateMovie(TmdbMovieIdSample.TheWolfOfWallStreet, rating) shouldBe Unit.right()
                }
            }
        }
    }

    Given("linked to Trakt") {
        authHelper.givenLinkedToTrakt()

        When("get all rated movies") {
            val getAllRatedMovies: GetAllRatedMovies by mockAppExtension.inject()

            Then("rated movies are emitted") {
                getAllRatedMovies().test {
                    awaitRemoteData() shouldBe listOf(
                        MovieWithPersonalRatingSample.Inception,
                        MovieWithPersonalRatingSample.TheWolfOfWallStreet
                    )
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }
})
