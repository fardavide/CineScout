package auth.tmdb

import assert4k.*
import domain.auth.StoreTmdbAccountId
import domain.auth.StoreTmdbCredentials
import domain.profile.GetPersonalTmdbProfile
import entities.TestData.DummyTmdbProfile
import entities.TmdbStringId
import entities.auth.Auth.LoginError.TokenApprovalCancelled
import entities.auth.Auth.LoginState.ApproveRequestToken
import entities.auth.Auth.LoginState.Completed
import entities.auth.Auth.LoginState.Loading
import entities.auth.Either_LoginResult
import entities.left
import entities.right
import io.ktor.http.fullPath
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import network.test.mockHttpClient
import network.test.respondJson
import util.test.CoroutinesTest
import kotlin.test.*

class AuthServiceTest : CoroutinesTest {

    private val client = mockHttpClient {
        addHandler { request ->
            when (val path = request.url.fullPath.substringAfter('/')) {
                "4/auth/request_token" -> respondJson(SuccessRequestTokenResponse)
                "4/auth/access_token" -> respondJson(SuccessAccessTokenResponse)
                "3/authentication/session/convert/4" -> respondJson(ForkV4TokenResponse)
                else -> error("Unhandled $path")
            }
        }
    }
    private val storeToken = mockk<StoreTmdbCredentials>(relaxed = true)
    private val storeTmdbAccountId = mockk<StoreTmdbAccountId>(relaxed = true)
    private val getProfile = mockk<GetPersonalTmdbProfile> {
        every { this@mockk() } returns flowOf(DummyTmdbProfile.right())
    }
    private val service = AuthService(client, storeToken, storeTmdbAccountId, getProfile)

    @Test
    fun `login can be resumed after user input`() = coroutinesTest {
        val result = mutableListOf<Either_LoginResult>()
        service.login().collect {
            result += it
            val right = it.rightOrNull()
            if (right is ApproveRequestToken.WithoutCode) {
                launch {
                    right.resultChannel.send(ApproveRequestToken.Approved.WithoutCode.right())
                }
            }
        }

        assert that result *{
            +size() equals 3
            +get(0).rightOrNull() equals Loading
            +get(1).rightOrNull() `is` type<ApproveRequestToken.WithoutCode>()
            +get(2).rightOrNull() equals Completed
        }
    }

    @Test
    fun `login can be stopped after user input`() = coroutinesTest {
        val result = mutableListOf<Either_LoginResult>()
        service.login().collect {
            result += it
            val right = it.rightOrNull()
            if (right is ApproveRequestToken.WithoutCode) {
                launch {
                    right.resultChannel.send(TokenApprovalCancelled.left())
                }
            }
        }

        assert that result *{
            +size() equals 3
            +get(0).rightOrNull() equals Loading
            +get(1).rightOrNull() `is` type<ApproveRequestToken.WithoutCode>()
            +get(2).leftOrNull() equals TokenApprovalCancelled
        }
    }

    @Test
    fun `credentials can be stored correctly`() = coroutinesTest {
        val result = mutableListOf<Either_LoginResult>()
        service.login().collect {
            result += it
            val right = it.rightOrNull()
            if (right is ApproveRequestToken.WithoutCode) {
                launch {
                    right.resultChannel.send(ApproveRequestToken.Approved.WithoutCode.right())
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
