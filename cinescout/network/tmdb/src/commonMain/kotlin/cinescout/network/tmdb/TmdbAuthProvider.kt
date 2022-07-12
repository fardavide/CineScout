package cinescout.network.tmdb

interface TmdbAuthProvider {

    fun accessToken(): String?

    fun accountId(): String?

    fun sessionId(): String?
}
