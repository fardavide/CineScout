package cinescout.account.trakt.data.remote

import arrow.core.Either
import cinescout.account.trakt.data.remote.model.GetAccount
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.trakt.TraktNetworkQualifier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path
import org.koin.core.annotation.Factory

@Factory
class TraktAccountService(
    @TraktNetworkQualifier.Client private val client: HttpClient
) {

    suspend fun getAccount(): Either<NetworkError, GetAccount.Response> = Either.Try {
        client.get { url.path("users", "settings") }.body()
    }
}
