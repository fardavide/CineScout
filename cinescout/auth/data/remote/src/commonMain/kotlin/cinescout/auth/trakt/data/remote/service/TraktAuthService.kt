package cinescout.auth.trakt.data.remote.service

import arrow.core.Either
import cinescout.auth.domain.model.TraktAuthorizationCode
import cinescout.auth.trakt.data.model.CreateAccessToken
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.trakt.TraktClient
import cinescout.network.trakt.TraktClientId
import cinescout.network.trakt.TraktClientSecret
import cinescout.network.trakt.TraktRedirectUrl
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.path
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class TraktAuthService(
    @Named(TraktClient) private val client: HttpClient,
    @Named(TraktClientId) private val clientId: String,
    @Named(TraktClientSecret) private val clientSecret: String,
    @Named(TraktRedirectUrl) private val redirectUrl: String
) {

    suspend fun createAccessToken(
        code: TraktAuthorizationCode
    ): Either<NetworkError, CreateAccessToken.Response> {
        val request = CreateAccessToken.Request.FromCode(
            code = code.value,
            clientId = clientId,
            clientSecret = clientSecret,
            redirectUri = redirectUrl,
            grantType = "authorization_code"
        )
        return createAccessToken(request)
    }

    private suspend fun createAccessToken(
        request: CreateAccessToken.Request
    ): Either<NetworkError, CreateAccessToken.Response> = Either.Try {
        client.post {
            url.path("oauth", "token")
            setBody(request)
        }.body()
    }
}
