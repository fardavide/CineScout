package cinescout.auth.tmdb.data

import app.cash.turbine.test
import arrow.core.right
import cinescout.auth.tmdb.data.model.TmdbAuthState
import cinescout.auth.tmdb.data.sample.TmdbAccessTokenAndAccountIdSample
import cinescout.auth.tmdb.data.sample.TmdbAuthorizedRequestTokenSample
import cinescout.auth.tmdb.data.sample.TmdbCredentialsSample
import cinescout.auth.tmdb.data.sample.TmdbRequestTokenSample
import cinescout.auth.tmdb.domain.repository.TmdbAuthRepository
import cinescout.auth.tmdb.domain.sample.LinkToTmdbStateSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.UnconfinedTestDispatcher

class RealTmdbAuthRepositoryTest : BehaviorSpec({

    Given("auth state is idle") {
        val authState = TmdbAuthState.Idle

        When("getting link status") {
            val scenario = TestScenario(authState = authState)
            scenario.isLinked().test {

                Then("not linked") {
                    awaitItem() shouldBe false
                }
            }
        }

        When("linking") {
            val scenario = TestScenario(authState = authState)
            scenario.link().test {

                Then("user should authorize token") {
                    awaitItem() shouldBe LinkToTmdbStateSample.UserShouldAuthorizeToken.right()
                }
            }
        }
    }

    Given("auth state is request token created") {
        val authState = TmdbAuthState.RequestTokenCreated(TmdbRequestTokenSample.RequestToken)

        When("getting link status") {
            val scenario = TestScenario(authState = authState)
            scenario.isLinked().test {

                Then("not linked") {
                    awaitItem() shouldBe false
                }
            }
        }

        When("linking") {
            val scenario = TestScenario(authState = authState)
            scenario.link().test {

                Then("user should authorize token") {
                    awaitItem() shouldBe LinkToTmdbStateSample.UserShouldAuthorizeToken.right()
                }
            }
        }
    }

    Given("auth state is request token authorized") {
        val authState = TmdbAuthState.RequestTokenAuthorized(TmdbAuthorizedRequestTokenSample.AuthorizedRequestToken)

        When("getting link status") {
            val scenario = TestScenario(authState = authState)
            scenario.isLinked().test {

                Then("not linked") {
                    awaitItem() shouldBe false
                }
            }
        }

        When("linking") {
            val scenario = TestScenario(authState = authState)
            scenario.link().test {

                And("user authorized token") {
                    scenario.notifyTokenAuthorized()

                    Then("success") {
                        awaitItem() shouldBe LinkToTmdbStateSample.Success.right()
                    }
                }
            }
        }
    }

    Given("auth state is access token created") {
        val authState = TmdbAuthState.AccessTokenCreated(TmdbAccessTokenAndAccountIdSample.AccessTokenAndAccountId)

        When("getting link status") {
            val scenario = TestScenario(authState = authState)
            scenario.isLinked().test {

                Then("not linked") {
                    awaitItem() shouldBe false
                }
            }
        }

        When("linking") {
            val scenario = TestScenario(authState = authState)
            scenario.link().test {

                And("user authorized token") {
                    scenario.notifyTokenAuthorized()

                    Then("success") {
                        awaitItem() shouldBe LinkToTmdbStateSample.Success.right()
                    }
                }
            }
        }
    }

    Given("auth state is completed") {
        val authState = TmdbAuthState.Completed(TmdbCredentialsSample.Credentials)

        When("getting link status") {
            val scenario = TestScenario(authState = authState)
            scenario.isLinked().test {

                Then("linked") {
                    awaitItem() shouldBe true
                }
            }
        }
    }
})

private class TestScenario(
    sut: RealTmdbAuthRepository
) : TmdbAuthRepository by sut

private fun TestScenario(authState: TmdbAuthState = TmdbAuthState.Idle) = TestScenario(
    sut = RealTmdbAuthRepository(
        dispatcher = UnconfinedTestDispatcher(),
        localDataSource = FakeTmdbAuthLocalDataSource(authState = authState),
        remoteDataSource = FakeTmdbAuthRemoteDataSource()
    )
)
