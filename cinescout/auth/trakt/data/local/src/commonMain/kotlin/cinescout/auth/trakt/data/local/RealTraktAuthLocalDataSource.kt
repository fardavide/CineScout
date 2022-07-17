package cinescout.auth.trakt.data.local

import cinescout.auth.trakt.data.TraktAuthLocalDataSource
import cinescout.auth.trakt.data.local.mapper.toDatabaseAccessToken
import cinescout.auth.trakt.data.local.mapper.toDatabaseRefreshToken
import cinescout.auth.trakt.data.local.mapper.toTokens
import cinescout.auth.trakt.data.model.TraktAccessAndRefreshTokens
import cinescout.database.TraktTokensQueries

class RealTraktAuthLocalDataSource(
    private val tokensQueries: TraktTokensQueries
) : TraktAuthLocalDataSource {

    override fun findTokensBlocking(): TraktAccessAndRefreshTokens? =
        tokensQueries.find().executeAsOneOrNull()?.toTokens()

    override suspend fun storeTokens(tokens: TraktAccessAndRefreshTokens) {
        tokensQueries.insertTokens(
            accessToken = tokens.accessToken.toDatabaseAccessToken(),
            refreshToken = tokens.refreshToken.toDatabaseRefreshToken()
        )
    }
}
