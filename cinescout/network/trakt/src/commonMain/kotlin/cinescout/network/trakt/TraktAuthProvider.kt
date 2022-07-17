package cinescout.network.trakt

interface TraktAuthProvider {

    fun accessToken(): String?

    fun refreshToken(): String?
}
