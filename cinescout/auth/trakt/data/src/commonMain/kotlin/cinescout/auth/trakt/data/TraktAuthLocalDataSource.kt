package cinescout.auth.trakt.data

import cinescout.auth.trakt.data.model.TraktAccessAndRefreshTokens

interface TraktAuthLocalDataSource {

    fun findTokensBlocking(): TraktAccessAndRefreshTokens?

    suspend fun storeTokens(tokens: TraktAccessAndRefreshTokens)
}
