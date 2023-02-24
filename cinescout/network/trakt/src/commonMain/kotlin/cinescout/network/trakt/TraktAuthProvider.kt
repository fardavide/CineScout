package cinescout.network.trakt

interface TraktAuthProvider {

    suspend fun accessToken(): String?

    fun invalidateTokens()

    suspend fun refreshToken(): String?
}

class FakeTraktAuthProvider : TraktAuthProvider {

    var accessTokenRequestedCount: Int = 0
        private set
    var invalidateTokensRequested: Boolean = false
        private set
    var refreshTokenRequestedCount: Int = 0
        private set

    override suspend fun accessToken(): String {
        accessTokenRequestedCount++
        return "access_token"
    }

    override fun invalidateTokens() {
        invalidateTokensRequested = true
    }

    override suspend fun refreshToken(): String {
        refreshTokenRequestedCount++
        return "refresh_token"
    }
}
