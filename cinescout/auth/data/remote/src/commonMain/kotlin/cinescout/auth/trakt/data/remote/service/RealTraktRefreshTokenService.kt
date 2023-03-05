package cinescout.auth.trakt.data.remote.service

import arrow.core.Either
import cinescout.auth.trakt.data.model.CreateAccessToken
import cinescout.auth.trakt.data.model.TraktRefreshToken
import cinescout.auth.trakt.data.service.TraktRefreshTokenService
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.trakt.TraktNetworkQualifier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.path
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class RealTraktRefreshTokenService(
    @Named(TraktNetworkQualifier.RefreshTokenClient) private val client: HttpClient,
    @Named(TraktNetworkQualifier.ClientId) private val clientId: String,
    @Named(TraktNetworkQualifier.ClientSecret) private val clientSecret: String,
    @Named(TraktNetworkQualifier.RedirectUrl) private val redirectUrl: String
) : TraktRefreshTokenService {

    override suspend fun createAccessToken(
        refreshToken: TraktRefreshToken
    ): Either<NetworkError, CreateAccessToken.Response> {
        val request = CreateAccessToken.Request.FromRefreshToken(
            refreshToken = refreshToken.value,
            clientId = clientId,
            clientSecret = clientSecret,
            redirectUri = redirectUrl,
            grantType = "refresh_token"
        )
        return Either.Try {
            client.post {
                url.path("oauth", "token")
                setBody(request)
            }.body()
        }
    }
}
