package profile.tmdb.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import profile.tmdb.remote.model.AccountResult

internal class AccountService(
    private val client: HttpClient
) {

    suspend fun getPersonalProfile(): AccountResult =
        client.get()
}
