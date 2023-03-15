package cinescout.network.trakt.model

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter

enum class TraktExtended(val value: String) {

    Episodes("episodes"),
    Full("full"),
    FullEpisodes("full,episodes"),
    Metadata("metadata"),
    NoSeasons("noseasons")
}

fun HttpRequestBuilder.extendedParameter(extended: TraktExtended) {
    parameter("extended", extended.value)
}
