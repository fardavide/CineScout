package cinescout.auth.tmdb.data.model

data class TmdbCredentials(
    val accessToken: TmdbAccessToken,
    val accountId: TmdbAccountId,
    val sessionId: TmdbSessionId
)