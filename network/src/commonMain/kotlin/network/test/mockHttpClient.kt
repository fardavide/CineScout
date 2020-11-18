package network.test

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.features.json.JsonFeature
import kotlinx.serialization.json.Json
import network.withEitherValidator

fun mockHttpClient(engineConfig: MockEngineConfig.() -> Unit) = HttpClient(MockEngine) {
    install(JsonFeature) {
        Json {}
    }
    engine(engineConfig)
    withEitherValidator()
}
