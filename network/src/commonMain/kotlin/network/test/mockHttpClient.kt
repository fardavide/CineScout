package network.test

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import kotlinx.serialization.json.Json
import network.withEitherValidator

fun mockHttpClient(engineConfig: MockEngineConfig.() -> Unit) = HttpClient(MockEngine) {
    install(JsonFeature) {
        Json {}
    }
    defaultRequest {
        headers.append(HttpHeaders.ContentType, ContentType.Application.Json)
    }
    engine(engineConfig)
    withEitherValidator()
}

fun MockRequestHandleScope.respondJson(json: String) =
    respond(
        json,
        headers = Headers.build {
            append(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    )
