package auth.tmdb.auth

import auth.tmdb.model.AccessTokenRequest
import auth.tmdb.model.AccessTokenResponse
import auth.tmdb.model.RequestTokenRequest
import auth.tmdb.model.RequestTokenResponse
import domain.auth.StoreTmdbAccessToken
import entities.Either
import entities.auth.Either_LoginResult
import entities.auth.TmdbAuth
import entities.auth.TmdbAuth.LoginError.TokenApprovalCancelled
import entities.auth.TmdbAuth.LoginState.ApproveRequestToken
import entities.auth.TmdbAuth.LoginState.ApproveRequestToken.Approved
import entities.auth.TmdbAuth.LoginState.Loading
import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import network.Try

internal class AuthService(
    client: HttpClient,
    private val storeToken: StoreTmdbAccessToken
) {

    private val client = client.config {
        defaultRequest {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }

    @Suppress("UNUSED_VARIABLE")
    fun login(): Flow<Either_LoginResult> = Either.fixFlow {
        emit(Loading)
        val (requestToken) = generateRequestToken().map { it.requestToken }
        val approveResultChannel = Channel<Either<TokenApprovalCancelled, Approved>>()
        emit(ApproveRequestToken(approveRequestTokenUrl(requestToken), approveResultChannel))
        val (approval) = approveResultChannel.receive()
        approveResultChannel.close()
        val (accessToken) = generateAccessToken(requestToken).map { it.accessToken }
        storeToken(accessToken)
        emit(TmdbAuth.LoginState.Completed)
    }

    private fun approveRequestTokenUrl(token: String) =
        "https://www.themoviedb.org/auth/access?request_token=$token"

    private suspend fun generateRequestToken() = Either.Try {
        client.post<RequestTokenResponse>(
            path = "auth/request_token",
            body = RequestTokenRequest("http://www.themoviedb.org/")
        )
    }

    private suspend fun generateAccessToken(requestToken: String) = Either.Try {
        client.post<AccessTokenResponse>(
            path = "auth/access_token",
            body = AccessTokenRequest(requestToken)
        )
    }

}
