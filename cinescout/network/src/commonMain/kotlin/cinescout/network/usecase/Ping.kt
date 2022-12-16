package cinescout.network.usecase

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.network.NetworkQualifier
import cinescout.network.Try
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.request

import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class Ping(
    @Named(NetworkQualifier.BaseHttpClient) private val client: HttpClient
) {

    suspend operator fun invoke(host: Host): Either<NetworkError, Unit> =
        Either.Try { client.request(host.url).body() }

    enum class Host(val url: String) {
        Google("https://google.com"),
        Tmdb("https://api.themoviedb.org"),
        Trakt("https://api.trakt.tv"),
    }
}
