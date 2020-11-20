package profile.tmdb.remote

import entities.Either
import entities.NetworkError
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import network.Try
import profile.tmdb.remote.model.AccountResult

internal class AccountService(
    private val client: HttpClient
) {

    suspend fun getPersonalProfile(): Either<NetworkError, AccountResult> = Either.Try {
        client.get(path = "3/account")
    }
}
