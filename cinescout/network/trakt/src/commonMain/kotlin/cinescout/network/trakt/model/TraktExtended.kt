package cinescout.network.trakt.model

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter
import io.ktor.http.Url

enum class TraktExtended(val value: String) {

    Full("full"),
    Metadata("metadata"),
}

fun HttpRequestBuilder.extendedParameter(extended: TraktExtended) {
    parameter("extended", extended.value)
}

fun Url.getExtendedParameter(): TraktExtended {
    val extended = parameters["extended"]
    return TraktExtended.values().find { it.value == extended } ?: TraktExtended.Metadata
}
