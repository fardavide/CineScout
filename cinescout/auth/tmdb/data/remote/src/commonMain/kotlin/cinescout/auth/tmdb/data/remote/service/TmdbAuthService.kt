package cinescout.auth.tmdb.data.remote.service

import arrow.core.Either
import cinescout.auth.tmdb.data.model.TmdbAccessToken
import cinescout.auth.tmdb.data.model.TmdbAuthorizedRequestToken
import cinescout.auth.tmdb.data.remote.model.ConvertV4Session
import cinescout.auth.tmdb.data.remote.model.CreateAccessToken
import cinescout.auth.tmdb.data.remote.model.CreateRequestToken
import cinescout.error.NetworkError
import cinescout.network.Try
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.path

internal class TmdbAuthService(
    private val v3client: HttpClient,
    private val v4client: HttpClient
) {

    suspend fun createRequestToken(): Either<NetworkError, CreateRequestToken.Response> =
        Either.Try {
            v4client.post { url.path("auth", "request_token") }.body()
        }

    suspend fun createAccessToken(token: TmdbAuthorizedRequestToken): Either<NetworkError, CreateAccessToken.Response> {
        val request = CreateAccessToken.Request(token.value)
        return Either.Try {
            v4client.post {
                url.path("auth", "access_token")
                setBody(request)
            }.body()
        }
    }

    suspend fun convertV4Session(token: TmdbAccessToken): Either<NetworkError, ConvertV4Session.Response> {
        val request = ConvertV4Session.Request(token.value)
        return Either.Try {
            v3client.post {
                url.path("authentication", "session", "convert", "4")
                setBody(request)
            }.body()
        }
    }
}
