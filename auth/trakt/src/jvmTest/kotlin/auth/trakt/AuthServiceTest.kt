package auth.trakt

import assert4k.*
import domain.auth.StoreTraktAccessToken
import entities.auth.Auth.LoginError.TokenApprovalCancelled
import entities.auth.Auth.LoginState.ApproveRequestToken
import entities.auth.Auth.LoginState.Completed
import entities.auth.Auth.LoginState.Loading
import entities.auth.Either_LoginResult
import entities.left
import entities.right
import io.ktor.http.fullPath
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import network.test.mockHttpClient
import network.test.respondJson
import util.test.CoroutinesTest
import kotlin.test.*

class AuthServiceTest : CoroutinesTest {

    private val client = mockHttpClient {
        addHandler { request ->
            when (val path = request.url.fullPath.substringAfter('/')) {
                "oauth/token" -> respondJson(SuccessAccessTokenResponse)
                else -> error("Unhandled $path")
            }
        }
    }
    private val storeToken = mockk<StoreTraktAccessToken>(relaxed = true)
    private val service = AuthService(client, "", "", storeToken)

    @Test
    fun `login can be resumed after user input`() = coroutinesTest {
        val result = mutableListOf<Either_LoginResult>()
        service.login().collect {
            result += it
            val right = it.rightOrNull()
            if (right is ApproveRequestToken.WithCode) {
                launch {
                    right.resultChannel.send(ApproveRequestToken.Approved.WithCode("").right())
                }
            }
        }

        assert that result *{
            +size() equals 3
            +get(0).rightOrNull() equals Loading
            +get(1).rightOrNull() `is` type<ApproveRequestToken.WithCode>()
            +get(2).rightOrNull() equals Completed
        }
    }

    @Test
    fun `login can be stopped after user input`() = coroutinesTest {
        val result = mutableListOf<Either_LoginResult>()
        service.login().collect {
            result += it
            val right = it.rightOrNull()
            if (right is ApproveRequestToken.WithCode) {
                launch {
                    right.resultChannel.send(TokenApprovalCancelled.left())
                }
            }
        }

        assert that result *{
            +size() equals 3
            +get(0).rightOrNull() equals Loading
            +get(1).rightOrNull() `is` type<ApproveRequestToken.WithCode>()
            +get(2).leftOrNull() equals TokenApprovalCancelled
        }
    }

    @Test
    fun `credentials can be stored correctly`() = coroutinesTest {
        val result = mutableListOf<Either_LoginResult>()
        service.login().collect {
            result += it
            val right = it.rightOrNull()
            if (right is ApproveRequestToken.WithCode) {
                launch {
                    right.resultChannel.send(ApproveRequestToken.Approved.WithCode("").right())
                }
            }
        }

        coVerify { storeToken("accessToken") }
    }

    private companion object {

        val SuccessAccessTokenResponse = """
            {
                "access_token": "accessToken",
                "token_type": "bearer",
                "expires_in": 7200,
                "refresh_token": "76ba4c5c75c96f6087f58a4de10be6c00b29ea1ddc3b2022ee2016d1363e3a7c",
                "scope": "public",
                "created_at": 1487889741
            }
            """.trimIndent()
    }
}
