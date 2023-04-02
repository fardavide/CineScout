package cinescout.network.tmdb

import cinescout.network.CineScoutClient
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
@Named(TmdbNetworkQualifier.Client)
fun CineScoutTmdbClient(): HttpClient = CineScoutClient {
    setup()
}

fun CineScoutTmdbClient(engine: HttpClientEngine, logBody: Boolean = false) =
    CineScoutClient(engine, logBody = logBody) { setup() }

private fun <T : HttpClientEngineConfig> HttpClientConfig<T>.setup() {
    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            host = "api.themoviedb.org/3"
            parameters.append("api_key", TMDB_V3_API_KEY)
        }
    }
}
