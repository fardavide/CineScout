package cinescout.auth.trakt.data.remote.service

import arrow.core.Either
import cinescout.auth.trakt.data.model.TraktRefreshToken
import cinescout.auth.trakt.data.remote.model.CreateAccessToken
import cinescout.auth.trakt.domain.model.TraktAuthorizationCode
import cinescout.error.NetworkError
import cinescout.network.Try
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.path

internal class TraktAuthService(
    private val client: HttpClient,
    private val clientId: String,
    private val clientSecret: String,
    private val redirectUrl: String
) {

    suspend fun createAccessToken(code: TraktAuthorizationCode): Either<NetworkError, CreateAccessToken.Response> {
        val request = CreateAccessToken.Request.FromCode(
            code = code.value,
            clientId = clientId,
            clientSecret = clientSecret,
            redirectUri = redirectUrl
        )
        return createAccessToken(request)
    }

    suspend fun createAccessToken(refreshToken: TraktRefreshToken): Either<NetworkError, CreateAccessToken.Response> {
        val request = CreateAccessToken.Request.FromRefreshToken(
            refreshToken = refreshToken.value,
            clientId = clientId,
            clientSecret = clientSecret,
            redirectUri = redirectUrl
        )
        return createAccessToken(request)
    }

    private suspend fun createAccessToken(
        request: CreateAccessToken.Request
    ): Either<NetworkError, CreateAccessToken.Response> =
        Either.Try {
            client.post {
                url.path("oauth", "token")
                setBody(request)
            }.body()
        }
}
