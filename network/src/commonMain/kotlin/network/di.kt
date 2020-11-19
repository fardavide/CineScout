package network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.logging.SIMPLE
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val baseHttpClient = named("Base Http client")

val networkModule = module {
    single(baseHttpClient) {
        HttpClient(ClientEngine) {
            install(JsonFeature) {
                Json {}
            }
             install(Logging) {
                 logger = Logger.SIMPLE
                 level = LogLevel.INFO
             }
            withEitherValidator()
        }
    }
}

internal expect val ClientEngine: HttpClientEngine
