package cinescout.account.tmdb.data.remote

import arrow.core.Either
import cinescout.account.tmdb.data.remote.model.GetAccount
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.tmdb.TmdbNetworkQualifier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
class TmdbAccountService(
    @Named(TmdbNetworkQualifier.V3.Client) private val client: HttpClient
) {

    suspend fun getAccount(): Either<NetworkError, GetAccount.Response> =
        Either.Try {
            client.get {
                url.path("account")
            }.body()
        }
}
