package auth.trakt

import auth.trakt.model.AccessTokenFromCodeRequest
import auth.trakt.model.AccessTokenRefreshTokenRequest
import auth.trakt.model.AccessTokenResponse
import entities.Either
import entities.auth.Auth.LoginError.TokenApprovalCancelled
import entities.auth.Auth.LoginState.ApproveRequestToken
import entities.auth.Auth.LoginState.Loading
import entities.auth.Either_LoginResult
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import network.Try

internal class AuthService(
    private val client: HttpClient,
    private val clientId: String,
    private val clientSecret: String
) {

    @Suppress("UNUSED_VARIABLE")
    fun login(): Flow<Either_LoginResult> = Either.fixFlow {
        emit(Loading)
        val approveResultChannel = Channel<Either<TokenApprovalCancelled, ApproveRequestToken.Approved.WithCode>>()
        emit(ApproveRequestToken.WithCode(authorizeAppUrl(), approveResultChannel))
        val (approval) = approveResultChannel.receive()
        approveResultChannel.close()
        val (accessTokenResponse) = generateAccessTokenFromCode(approval.code)
        TODO("store credentials")
    }

    private fun authorizeAppUrl() =
        "https://api.trakt.tv/oauth/authorize?response_type=code&client_id=$clientId&redirect_uri=$RedirectUrl"

    private suspend fun generateAccessTokenFromCode(code: String) = Either.Try {
        client.get<AccessTokenResponse>(
            path = "oauth/token",
            body = AccessTokenFromCodeRequest(
                clientId = clientId,
                clientSecret = clientSecret,
                code = code,
                redirectUri = RedirectUrl,
                grantType = "authorization_code"
            )
        )
    }

    private suspend fun generateAccessTokenFromRefreshToken(refreshToken: String) = Either.Try {
        client.get<AccessTokenResponse>(
            path = "oauth/token",
            body = AccessTokenRefreshTokenRequest(
                clientId = clientId,
                clientSecret = clientSecret,
                refreshToken = refreshToken,
                redirectUri = RedirectUrl,
                grantType = "refresh_token"
            )
        )
    }

    private companion object {
        const val RedirectUrl = "cinescout://app"
    }
}
