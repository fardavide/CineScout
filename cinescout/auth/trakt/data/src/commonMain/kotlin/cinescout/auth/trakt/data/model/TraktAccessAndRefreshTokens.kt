package cinescout.auth.trakt.data.model

data class TraktAccessAndRefreshTokens(
    val accessToken: TraktAccessToken,
    val refreshToken: TraktRefreshToken
)
