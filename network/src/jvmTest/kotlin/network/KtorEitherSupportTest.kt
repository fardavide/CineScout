package network

import assert4k.*
import entities.Either
import entities.NetworkError
import entities.left
import entities.right
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath
import util.test.CoroutinesTest
import kotlin.test.*

internal class KtorEitherSupportTest: CoroutinesTest {

    class TestService(private val client: HttpClient) {

        suspend fun success() = Either.Try {
            client.get<String>("success")
        }

        suspend fun notFound() = Either.Try {
            client.get<String>("not_found")
        }
    }

    private val client = HttpClient(MockEngine) {
        engine {
            addHandler { request ->

                val url = request.url.fullPath.substringAfter('/')
                fun error(code: Int) = respond("url", HttpStatusCode(code, url))

                when (url) {
                    "success" -> respond("hello")
                    "not_found" -> error(404)
                    else -> error("Unhandled $url")
                }
            }
        }
        withEitherValidator()
    }
    private val service = TestService(client)

    @Test
    fun `correctly create an Either of String`() = coroutinesTest {
        assert that service.success() equals "hello".right()
    }

    @Test
    fun `correctly create an Either of NotFound error`() = coroutinesTest {
        assert that service.notFound() equals NetworkError.NotFound.left()
    }
}
