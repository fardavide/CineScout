package tests

import app.cash.turbine.test
import cinescout.movies.domain.sample.MovieWithPersonalRatingSample
import cinescout.movies.domain.usecase.GetAllRatedMovies
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

