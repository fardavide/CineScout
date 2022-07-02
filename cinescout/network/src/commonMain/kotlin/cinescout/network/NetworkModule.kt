package cinescout.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.header
import io.ktor.http.ContentType.Application
import io.ktor.http.HttpHeaders.ContentType
import io.ktor.serialization.kotlinx.json.json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val NetworkModule = module {

    factory(NetworkQualifier.BaseHttpClient) {
        HttpClient {

            install(ContentNegotiation) {
                json()
            }
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.INFO
            }
            defaultRequest {
                header(ContentType, Application.Json)
            }
            withEitherValidator()
        }
    }
}

object NetworkQualifier {
    val BaseHttpClient = named("Base Http client")
}
