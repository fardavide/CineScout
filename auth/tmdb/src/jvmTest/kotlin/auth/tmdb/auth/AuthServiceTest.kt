package auth.tmdb.auth

import assert4k.*
import domain.auth.StoreTmdbAccountId
import domain.auth.StoreTmdbCredentials
import domain.profile.GetPersonalTmdbProfile
import entities.TestData.DummyProfile
import entities.TmdbStringId
import entities.auth.Auth.LoginError.TokenApprovalCancelled
import entities.auth.Auth.LoginState.ApproveRequestToken
import entities.auth.Auth.LoginState.Completed
import entities.auth.Auth.LoginState.Loading
import entities.auth.Either_LoginResult
import entities.left
import entities.right
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import io.ktor.http.fullPath
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import network.test.mockHttpClient
import util.test.CoroutinesTest
import kotlin.test.*

class AuthServiceTest : CoroutinesTest {

    private val client = mockHttpClient {
        addHandler { request ->
            val jsonHeader = Headers.build {
                append(HttpHeaders.ContentType, ContentType.Application.Json)
            }

            when (val path = request.url.fullPath.substringAfter('/')) {
                "4/auth/request_token" -> respond(SuccessRequestTokenResponse, headers = jsonHeader)
                "4/auth/access_token" -> respond(SuccessAccessTokenResponse, headers = jsonHeader)
                "3/authentication/session/convert/4" -> respond(ForkV4TokenResponse, headers = jsonHeader)
                else -> error("Unhandled $path")
            }
        }
    }
    private val storeToken = mockk<StoreTmdbCredentials>(relaxed = true)
    private val storeTmdbAccountId = mockk<StoreTmdbAccountId>(relaxed = true)
    private val getProfile = mockk<GetPersonalTmdbProfile> {
        every { this@mockk() } returns flowOf(DummyProfile.right())
    }
    private val service = AuthService(client, storeToken, storeTmdbAccountId, getProfile)

    @Test
    fun `login can be resumed after user input`() = coroutinesTest {
        val result = mutableListOf<Either_LoginResult>()
        service.login().collect {
            result += it
            val right = it.rightOrNull()
            if (right is ApproveRequestToken) {
                launch {
                    right.resultChannel.send(ApproveRequestToken.Approved.right())
                }
            }
        }

        assert that result *{
            +size() equals 3
            +get(0).rightOrNull() equals Loading
            +get(1).rightOrNull() `is` type<ApproveRequestToken>()
            +get(2).rightOrNull() equals Completed
        }
    }

    @Test
    fun `login can be stopped after user input`() = coroutinesTest {
        val result = mutableListOf<Either_LoginResult>()
        service.login().collect {
            result += it
            val right = it.rightOrNull()
            if (right is ApproveRequestToken) {
                launch {
                    right.resultChannel.send(TokenApprovalCancelled.left())
                }
            }
        }

        assert that result *{
            +size() equals 3
            +get(0).rightOrNull() equals Loading
            +get(1).rightOrNull() `is` type<ApproveRequestToken>()
            +get(2).leftOrNull() equals TokenApprovalCancelled
        }
    }

    @Test
    fun `credentials can be stored correctly`() = coroutinesTest {
        val result = mutableListOf<Either_LoginResult>()
        service.login().collect {
            result += it
            val right = it.rightOrNull()
            if (right is ApproveRequestToken) {
                launch {
                    right.resultChannel.send(ApproveRequestToken.Approved.right())
                }
            }
        }

        coVerify { storeToken(TmdbStringId("accountId"), "accessToken", "sessionId") }
    }

    private companion object {

        val SuccessRequestTokenResponse = """
            { 
                "request_token": "requestToken",
                "status_code": 200,
                "status_message": "success",
                "success": true
            }
            """.trimIndent()

        val SuccessAccessTokenResponse = """
            { 
                "access_token": "accessToken",
                "status_code": 200,
                "status_message": "success",
                "success": true,
                "account_id": "accountId"
            }
            """.trimIndent()

        val ForkV4TokenResponse = """
            {
                "success": true,
                "session_id": "sessionId"
            }
        """.trimIndent()
    }
}
