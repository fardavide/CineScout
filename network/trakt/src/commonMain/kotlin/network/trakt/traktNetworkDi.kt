package network.trakt

import io.ktor.client.HttpClient
import network.baseHttpClient
import network.networkModule
import org.koin.core.qualifier.named
import org.koin.dsl.module

val client = named("Trakt client")

val traktNetworkModule = module {

    single(client) {
        get<HttpClient>(baseHttpClient).config {

        }
    }

} + networkModule
