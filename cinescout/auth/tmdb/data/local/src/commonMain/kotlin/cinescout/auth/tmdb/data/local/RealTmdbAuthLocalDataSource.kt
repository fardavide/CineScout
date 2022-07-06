package cinescout.auth.tmdb.data.local

import cinescout.auth.tmdb.data.TmdbAuthLocalDataSource
import cinescout.auth.tmdb.data.local.mapper.toDatabaseAccessToken
import cinescout.auth.tmdb.data.local.mapper.toDatabaseAccountId
import cinescout.auth.tmdb.data.local.mapper.toDatabaseSessionId
import cinescout.auth.tmdb.data.model.TmdbAccessToken
import cinescout.auth.tmdb.data.model.TmdbCredentials
import cinescout.database.TmdbCredentialsQueries

class RealTmdbAuthLocalDataSource(
    private val credentialsQueries: TmdbCredentialsQueries
) : TmdbAuthLocalDataSource {

    @Deprecated("Use storeCredentials instead")
    override suspend fun storeAccessToken(accessToken: TmdbAccessToken) {
        TODO()
    }

    override suspend fun storeCredentials(credentials: TmdbCredentials) {
        credentialsQueries.insertCredentials(
            accessToken = credentials.accessToken.toDatabaseAccessToken(),
            accountId = credentials.accountId.toDatabaseAccountId(),
            sessionId = credentials.sessionId.toDatabaseSessionId()
        )
    }
}
