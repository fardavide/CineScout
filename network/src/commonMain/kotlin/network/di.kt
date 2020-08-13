package network

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
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
        }
    }
}

internal expect val ClientEngine: HttpClientEngine
