package tests

import app.cash.turbine.test
import arrow.core.nonEmptyListOf
import arrow.core.right
import cinescout.screenplay.domain.model.Rating
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.usecase.GenerateSuggestedTvShows
import cinescout.test.mock.junit5.MockAppExtension
import cinescout.tvshows.domain.sample.TmdbTvShowIdSample
import cinescout.tvshows.domain.sample.TvShowSample
import cinescout.tvshows.domain.sample.TvShowWithDetailsSample
import cinescout.tvshows.domain.sample.TvShowWithPersonalRatingSample
import cinescout.tvshows.domain.usecase.GetAllRatedTvShows
import cinescout.tvshows.domain.usecase.GetAllWatchlistTvShows
import cinescout.tvshows.domain.usecase.GetTvShowDetails
import cinescout.tvshows.domain.usecase.RateTvShow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.koin.test.inject
import util.AuthHelper
import util.awaitRemoteData

class TvShowsTest : BehaviorSpec({
    val mockAppExtension = MockAppExtension {
        appScope(TestScope(UnconfinedTestDispatcher()))
    }
    extensions(mockAppExtension)

    val authHelper = AuthHelper()

    Given("linked to Tmdb") {
        authHelper.givenLinkedToTmdb()

        When("get tvShow details") {
            val getTvShowDetails: GetTvShowDetails by mockAppExtension.inject()

            Then("tvShow is emitted") {
                getTvShowDetails(TmdbTvShowIdSample.Grimm).test {
                    awaitItem() shouldBe TvShowWithDetailsSample.Grimm.right()
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        When("get all rated tvShows") {
            val getAllRatedTvShows: GetAllRatedTvShows by mockAppExtension.inject()

            Then("rated tvShows are emitted") {
                getAllRatedTvShows().test {
                    awaitRemoteData() shouldBe listOf(TvShowWithPersonalRatingSample.Grimm)
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        When("get all watchlist tvShows") {
            val getAllWatchlistTvShows: GetAllWatchlistTvShows by mockAppExtension.inject()

            Then("watchlist tvShows are emitted") {
                getAllWatchlistTvShows().test {
                    awaitRemoteData() shouldBe listOf(TvShowSample.Grimm)
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        When("generate quick suggested tvShows") {
            val generateSuggestedTvShows: GenerateSuggestedTvShows by mockAppExtension.inject()

            Then("suggested tvShows are emitted") {
                generateSuggestedTvShows(SuggestionsMode.Quick).test {
                    awaitItem() shouldBe nonEmptyListOf(TvShowSample.BreakingBad).right()
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        When("rate tvShow") {
            val rateTvShow: RateTvShow by mockAppExtension.inject()

            Then("success") {
                Rating.of(8).tap { rating ->
                    rateTvShow(TmdbTvShowIdSample.Dexter, rating) shouldBe Unit.right()
                }
            }
        }
    }

    Given("linked to Trakt") {
        authHelper.givenLinkedToTrakt()

        When("get all rated tvShows") {
            val getAllRatedTvShows: GetAllRatedTvShows by mockAppExtension.inject()

            Then("rated tvShows are emitted") {
                getAllRatedTvShows().test {
                    awaitRemoteData() shouldBe listOf(TvShowWithPersonalRatingSample.Grimm)
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }
})
