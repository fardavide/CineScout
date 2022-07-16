package cinescout.auth.trakt.data

import cinescout.auth.trakt.data.model.TraktAccessAndRefreshTokens

interface TraktAuthLocalDataSource {

    suspend fun storeTokens(tokens: TraktAccessAndRefreshTokens)
}
