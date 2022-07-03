package cinescout.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json

fun CineScoutClient(): HttpClient = HttpClient {
    setup()
}

fun CineScoutClient(engine: HttpClientEngine) = HttpClient(engine) {
    setup()
}

private fun <T : HttpClientEngineConfig> HttpClientConfig<T>.setup() {
    install(ContentNegotiation) {
        json()
    }
    install(Logging) {
        logger = Logger.SIMPLE
        level = LogLevel.INFO
    }
    defaultRequest {
        header(HttpHeaders.ContentType, ContentType.Application.Json)
    }
    withEitherValidator()
}
