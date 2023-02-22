package cinescout.network.usecase

import arrow.core.Either
import arrow.core.Nel
import arrow.core.left
import arrow.core.nonEmptyListOf
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.network.NetworkQualifier
import cinescout.network.Try
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.request
import kotlinx.coroutines.time.delay

import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import kotlin.time.Duration
import kotlin.time.toJavaDuration

interface Ping {

    suspend operator fun invoke(host: Host): Either<NetworkError, Unit>

    enum class Host(val url: String) {
        Google("https://google.com"),
        Tmdb("https://api.themoviedb.org"),
        Trakt("https://api.trakt.tv")
    }
}

@Factory
internal class RealPing(
    @Named(NetworkQualifier.BaseHttpClient) private val client: HttpClient
) : Ping {

    override suspend operator fun invoke(host: Ping.Host): Either<NetworkError, Unit> =
        Either.Try { client.request(host.url).body() }
}

class FakePing(
    private val delay: Duration = Duration.ZERO,
    private val alternateFailures: Boolean = false,
    private val results: Nel<Either<NetworkError, Unit>> =
        if (alternateFailures) nonEmptyListOf(Unit.right(), NetworkError.Unreachable.left())
        else nonEmptyListOf(Unit.right())
) : Ping {

    private var i = 0
    private val hostInvocations = mutableMapOf<Ping.Host, Int>()

    override suspend operator fun invoke(host: Ping.Host): Either<NetworkError, Unit> {
        hostInvocations[host] = hostInvocations.getOrDefault(host, 0) + 1
        delay(delay.toJavaDuration())
        return results[i++ % results.size]
    }

    fun invocationCount(host: Ping.Host): Int = hostInvocations.getOrDefault(host, 0)
}
