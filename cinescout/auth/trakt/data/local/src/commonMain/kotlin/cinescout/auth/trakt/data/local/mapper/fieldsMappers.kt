package cinescout.auth.trakt.data.local.mapper

import cinescout.auth.trakt.data.model.TraktAccessAndRefreshTokens
import cinescout.auth.trakt.data.model.TraktAccessToken
import cinescout.auth.trakt.data.model.TraktRefreshToken
import cinescout.database.model.DatabaseTraktAccessToken
import cinescout.database.model.DatabaseTraktRefreshToken
import cinescout.database.model.DatabaseTraktTokens

fun TraktAccessToken.toDatabaseAccessToken() = DatabaseTraktAccessToken(value)
fun TraktRefreshToken.toDatabaseRefreshToken() = DatabaseTraktRefreshToken(value)
fun TraktAccessAndRefreshTokens.toDatabaseTokens() = DatabaseTraktTokens(
    id = 0,
    accessToken = accessToken.toDatabaseAccessToken(),
    refreshToken = refreshToken.toDatabaseRefreshToken()
)
