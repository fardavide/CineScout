package cinescout.auth.tmdb.data.remote.service

import arrow.core.Either
import cinescout.auth.tmdb.data.model.TmdbAuthorizedRequestToken
import cinescout.auth.tmdb.data.remote.model.CreateAccessToken
import cinescout.auth.tmdb.data.remote.model.CreateRequestToken
import cinescout.error.NetworkError
import cinescout.network.Try
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.path

class TmdbAuthService(
    private val client: HttpClient
) {

    suspend fun createRequestToken(): Either<NetworkError, CreateRequestToken.Response> =
        Either.Try {
            client.post { url.path("auth", "request_token") }.body()
        }

    suspend fun createAccessToken(token: TmdbAuthorizedRequestToken): Either<NetworkError, CreateAccessToken.Response> {
        val request = CreateAccessToken.Request(token.value)
        return Either.Try {
            client.post {
                url.path("auth", "access_token")
                setBody(request)
            }.body()
        }
    }
}
