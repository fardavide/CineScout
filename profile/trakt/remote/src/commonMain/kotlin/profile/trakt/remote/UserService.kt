package profile.trakt.remote

import entities.Either
import entities.NetworkError
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import network.Try
import profile.trakt.remote.model.SettingsResult

internal class UserService (
    private val client: HttpClient
) {

    suspend fun getPersonalProfile(): Either<NetworkError, SettingsResult> = Either.Try {
        client.get(path = "users/settings")
    }
}
