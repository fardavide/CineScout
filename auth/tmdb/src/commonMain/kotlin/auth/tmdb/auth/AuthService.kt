package auth.tmdb.auth

import auth.tmdb.model.AccessTokenRequest
import auth.tmdb.model.AccessTokenResponse
import auth.tmdb.model.ForkV4TokenRequest
import auth.tmdb.model.ForkV4TokenResponse
import auth.tmdb.model.RequestTokenRequest
import auth.tmdb.model.RequestTokenResponse
import domain.auth.StoreTmdbAccountId
import domain.auth.StoreTmdbCredentials
import domain.profile.GetPersonalTmdbProfile
import entities.Either
import entities.TmdbOauthCallback
import entities.TmdbStringId
import entities.auth.Auth
import entities.auth.Auth.LoginError.TokenApprovalCancelled
import entities.auth.Auth.LoginState.ApproveRequestToken
import entities.auth.Auth.LoginState.ApproveRequestToken.Approved
import entities.auth.Auth.LoginState.Loading
import entities.auth.Either_LoginResult
import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import network.Try

internal class AuthService(
    client: HttpClient,
    private val storeCredentials: StoreTmdbCredentials,
    private val storeTmdbAccountId: StoreTmdbAccountId,
    private val getProfile: GetPersonalTmdbProfile
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
        val (accessTokenResponse) = generateAccessToken(requestToken)
        val (forkTokenResponse) = forkV4Session(accessTokenResponse.accessToken)
        storeCredentials(
            TmdbStringId(accessTokenResponse.accountId),
            accessTokenResponse.accessToken,
            forkTokenResponse.sessionId
        )
        val (profile) = getProfile().filter { it.isRight() }.first()
        storeTmdbAccountId.invoke(profile.id)
        emit(Auth.LoginState.Completed)
    }

    private fun approveRequestTokenUrl(token: String) =
        "https://www.themoviedb.org/auth/access?request_token=$token"

    private suspend fun generateRequestToken() = Either.Try {
        client.post<RequestTokenResponse>(
            path = "4/auth/request_token",
            body = RequestTokenRequest(TmdbOauthCallback)
        )
    }

    private suspend fun generateAccessToken(requestToken: String) = Either.Try {
        client.post<AccessTokenResponse>(
            path = "4/auth/access_token",
            body = AccessTokenRequest(requestToken)
        )
    }

    private suspend fun forkV4Session(accessToken: String) = Either.Try {
        client.post<ForkV4TokenResponse>(
            path = "3/authentication/session/convert/4",
            body = ForkV4TokenRequest(accessToken)
        )
    }

}
