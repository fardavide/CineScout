package cinescout.auth.trakt.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.auth.trakt.data.model.CreateAccessToken
import cinescout.auth.trakt.data.model.TraktAccessAndRefreshTokens
import cinescout.auth.trakt.data.sample.CreateAccessTokenResponseSample
import cinescout.auth.trakt.data.sample.TraktAccessAndRefreshTokensSample
import cinescout.auth.trakt.data.service.FakeTraktRefreshTokenService
import cinescout.error.NetworkError
import cinescout.network.trakt.FakeTraktAuthProvider
import cinescout.network.trakt.error.RefreshTokenError
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class RealRefreshTraktAccessTokenTest : BehaviorSpec({

    Given("logged in") {
        val tokens = TraktAccessAndRefreshTokensSample.Tokens

        When("success refreshing access token") {
            val refreshTokenResult = CreateAccessTokenResponseSample.Refreshed.right()
            val scenario = TestScenario(refreshTokenResult, tokens)
            val result = scenario.sut()

            Then("result is success") {
                result shouldBe Unit.right()
            }

            Then("access token should be refreshed in the local data source") {
                scenario.localDataSource.findTokens() shouldBe TraktAccessAndRefreshTokensSample.RefreshedTokens
            }

            Then("tokens should be invalidated in the auth provider") {
                scenario.authProvider.invalidateTokensRequested shouldBe true
            }
        }

        When("failure refreshing access token") {
            val error = NetworkError.Internal
            val scenario = TestScenario(refreshTokenResult = error.left(), tokens)
            val result = scenario.sut()

            Then("result is network error") {
                result shouldBe RefreshTokenError.Network(error).left()
            }
        }
    }

    Given("not logged in") {
        val refreshTokenResult = NetworkError.Unauthorized.left()
        val scenario = TestScenario(refreshTokenResult, tokens = null)

        When("refreshing access token") {
            val result = scenario.sut()

            Then("result is no refresh token error") {
                result shouldBe RefreshTokenError.NoRefreshToken.left()
            }
        }
    }
})

private class RealRefreshTraktAccessTokenTestScenario(
    val sut: RealRefreshTraktAccessToken,
    val authProvider: FakeTraktAuthProvider,
    val localDataSource: FakeTraktAuthLocalDataSource
)

private fun TestScenario(
    refreshTokenResult: Either<NetworkError, CreateAccessToken.Response>,
    tokens: TraktAccessAndRefreshTokens?
): RealRefreshTraktAccessTokenTestScenario {
    val authProvider = FakeTraktAuthProvider()
    val localDataSource = FakeTraktAuthLocalDataSource(tokens = tokens)
    return RealRefreshTraktAccessTokenTestScenario(
        sut = RealRefreshTraktAccessToken(
            authProvider = authProvider,
            localDataSource = localDataSource,
            refreshTokenService = FakeTraktRefreshTokenService(refreshTokenResult)
        ),
        authProvider = authProvider,
        localDataSource = localDataSource
    )
}
