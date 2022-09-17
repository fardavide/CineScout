package cinescout.network.trakt

interface TraktAuthProvider {

    suspend fun accessToken(): String?

    suspend fun refreshToken(): String?
}
