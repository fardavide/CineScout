package cinescout.network.trakt.model

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter

enum class TraktMovieExtended(val value: String) {

    Full("full"),
    Metadata("metadata")
}

enum class TraktTvShowExtended(val value: String) {

    Episodes("episodes"),
    Full("full"),
    FullEpisodes("full,episodes"),
    Metadata("metadata"),
    NoSeasons("noseasons")
}

fun HttpRequestBuilder.movieExtended(extended: TraktMovieExtended) {
    parameter("extended", extended.value)
}

fun HttpRequestBuilder.tvShowExtended(extended: TraktTvShowExtended) {
    parameter("extended", extended.value)
}
