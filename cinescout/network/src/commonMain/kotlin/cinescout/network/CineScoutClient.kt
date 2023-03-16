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
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun CineScoutClient(block: HttpClientConfig<*>.() -> Unit = {}): HttpClient = HttpClient {
    setup()
    block()
}

fun CineScoutClient(engine: HttpClientEngine, block: HttpClientConfig<*>.() -> Unit = {}) =
    HttpClient(engine) {
        setup()
        block()
    }

private fun <T : HttpClientEngineConfig> HttpClientConfig<T>.setup() {
    install(ContentNegotiation) {
        json(
            json = Json {
                ignoreUnknownKeys = true
                serializersModule = CineScoutSerializersModule
            }
        )
    }
    install(Logging) {
        logger = Logger.SIMPLE
        level = LogLevel.INFO
    }
    defaultRequest {
        contentType(ContentType.Application.Json)
    }
    withEitherValidator()
}
