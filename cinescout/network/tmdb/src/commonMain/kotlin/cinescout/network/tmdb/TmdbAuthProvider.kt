package cinescout.network.tmdb

interface TmdbAuthProvider {

    suspend fun accessToken(): String?

    suspend fun accountId(): String?

    suspend fun sessionId(): String?
}
