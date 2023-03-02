package cinescout.auth.trakt.data

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.right
import cinescout.auth.trakt.data.model.TraktAuthState
import cinescout.auth.trakt.data.sample.LinkToTraktStateSample
import cinescout.auth.trakt.data.sample.TraktAccessAndRefreshTokensSample
import cinescout.auth.trakt.domain.TraktAuthRepository
import cinescout.auth.trakt.domain.sample.TraktAuthorizationCodeSample
import cinescout.auth.trakt.domain.usecase.LinkToTrakt
import cinescout.error.NetworkError
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher

class RealTraktAuthRepositoryTest : BehaviorSpec({

    Given("auth state is idle") {
        val authState = TraktAuthState.Idle

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

            Then("user should authorize app") {

                scenario.link().test {
                    val either = awaitItem()
                    val state = either.shouldBeInstanceOf<Either.Right<LinkToTrakt.State.UserShouldAuthorizeApp>>()
                    state.value.authorizationUrl shouldBe LinkToTraktStateSample.AppAuthenticationUrl
                }
            }
        }

        When("unlinking") {
            val scenario = TestScenario(authState = authState)
            scenario.unlink()

            Then("not linked") {
                scenario.isLinked().test {
                    awaitItem() shouldBe false
                }
            }

            And("tokens should be cleared") {
                scenario.localDataSource.findTokens() shouldBe null
            }
        }
    }

    Given("auth state is app authorized") {
        val code = TraktAuthorizationCodeSample.AuthorizationCode
        val authState = TraktAuthState.AppAuthorized(code)

        When("getting link status") {
            val scenario = TestScenario(authState = authState)
            scenario.isLinked().test {

                Then("not linked") {
                    awaitItem() shouldBe false
                }
            }
        }

        When("linking") {

            And("user authorized app") {
                val scenario = TestScenario(authState = authState)
                scenario.notifyAppAuthorized(code)

                Then("success") {
                    scenario.link().test {
                        awaitItem() shouldBe LinkToTraktStateSample.Success.right()
                    }
                }
            }
            
            And("create access token returns a bad request") {
                val scenario = TestScenario(
                    authState = authState,
                    createAccessTokenFirstCallError = NetworkError.BadRequest
                )

                Then("link flow is restarted") {
                    scenario.link().test {
                        awaitItem().getOrNull().shouldBeInstanceOf<LinkToTrakt.State.UserShouldAuthorizeApp>()
                        scenario.notifyAppAuthorized(code)
                        awaitItem() shouldBe LinkToTraktStateSample.Success.right()
                    }
                }
            }
        }

        When("unlinking") {
            val scenario = TestScenario(authState = authState)
            scenario.unlink()

            Then("not linked") {
                scenario.isLinked().test {
                    awaitItem() shouldBe false
                }
            }

            And("tokens should be cleared") {
                scenario.localDataSource.findTokens() shouldBe null
            }
        }
    }

    Given("auth state is completed") {
        val authState = TraktAuthState.Completed(TraktAccessAndRefreshTokensSample.Tokens)

        When("getting link status") {
            val scenario = TestScenario(authState = authState)
            scenario.isLinked().test {

                Then("linked") {
                    awaitItem() shouldBe true
                }
            }
        }

        When("linking") {
            val scenario = TestScenario(authState = authState)
            scenario.link().test {

                Then("already linked") {
                    awaitItem() shouldBe LinkToTraktStateSample.Success.right()
                }
            }
        }

        When("unlinking") {
            val scenario = TestScenario(authState = authState)
            scenario.unlink()

            Then("not linked") {
                scenario.isLinked().test {
                    awaitItem() shouldBe false
                }
            }

            And("tokens should be cleared") {
                scenario.localDataSource.findTokens() shouldBe null
            }
        }
    }
})

private class TestScenario(
    sut: RealTraktAuthRepository,
    val localDataSource: FakeTraktAuthLocalDataSource
) : TraktAuthRepository by sut

private fun TestScenario(
    authState: TraktAuthState = TraktAuthState.Idle,
    createAccessTokenFirstCallError: NetworkError? = null
): TestScenario {
    val localDataSource = FakeTraktAuthLocalDataSource(authState = authState)
    return TestScenario(
        sut = RealTraktAuthRepository(
            dispatcher = UnconfinedTestDispatcher(),
            localDataSource = localDataSource,
            remoteDataSource = FakeTraktAuthRemoteDataSource(
                createAccessTokenFirstCallError = createAccessTokenFirstCallError
            )
        ),
        localDataSource = localDataSource
    )
}
