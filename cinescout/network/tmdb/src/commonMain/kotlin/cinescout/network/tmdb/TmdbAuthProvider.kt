package cinescout.network.tmdb

interface TmdbAuthProvider {

    fun accessToken(): String?

    fun sessionId(): String?
}
