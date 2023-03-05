package tests

import app.cash.turbine.test
import cinescout.test.mock.junit5.MockAppExtension
import cinescout.tvshows.domain.sample.TvShowWithPersonalRatingSample
import cinescout.tvshows.domain.usecase.GetAllRatedTvShows
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

