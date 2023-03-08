package cinescout.network.trakt

interface TraktAuthProvider {

    suspend fun accessToken(): String?

    fun invalidateTokens()

    suspend fun refreshToken(): String?
}

class FakeTraktAuthProvider(
    private var isConnected: Boolean = true
) : TraktAuthProvider {

    var accessTokenRequestedCount: Int = 0
        private set
    var invalidateTokensRequested: Boolean = false
        private set
    var refreshTokenRequestedCount: Int = 0
        private set

    override suspend fun accessToken(): String? {
        accessTokenRequestedCount++
        return if (isConnected) "access_token" else null
    }

    override fun invalidateTokens() {
        invalidateTokensRequested = true
        isConnected = false
    }

    override suspend fun refreshToken(): String? {
        refreshTokenRequestedCount++
        return if (isConnected) "refresh_token" else null
    }
}
