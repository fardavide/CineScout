package auth.tmdb.auth

import assert4k.*
import domain.auth.StoreTmdbAccessToken
import entities.auth.Either_LoginResult
import entities.auth.TmdbAuth.LoginError.TokenApprovalCancelled
import entities.auth.TmdbAuth.LoginState.ApproveRequestToken
import entities.auth.TmdbAuth.LoginState.Completed
import entities.auth.TmdbAuth.LoginState.Loading
import entities.left
import entities.right
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.features.json.JsonFeature
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import io.ktor.http.fullPath
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import network.withEitherValidator
import util.test.CoroutinesTest
import kotlin.test.*

class AuthServiceTest : CoroutinesTest {

    private val client = HttpClient(MockEngine) {
        install(JsonFeature) {
            Json {}
        }
        engine {
            addHandler { request ->
                val jsonHeader = Headers.build {
                    append(HttpHeaders.ContentType, ContentType.Application.Json)
                }

                when (val path = request.url.fullPath.substringAfter('/')) {
                    "auth/request_token" -> respond(SuccessRequestTokenResponse, headers = jsonHeader)
                    "auth/access_token" -> respond(SuccessAccessTokenResponse, headers = jsonHeader)
                    else -> error("Unhandled $path")
                }
            }
        }
        withEitherValidator()
    }
    private val storeToken = mockk<StoreTmdbAccessToken>(relaxed = true)
    private val service = AuthService(client, storeToken)

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
    fun `token can be stored correctly`() = coroutinesTest {
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

        coVerify { storeToken("accessToken") }
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
                "account_id": "account"
            }
            """.trimIndent()
    }
}
