package cinescout.auth.tmdb.data.local

import cinescout.auth.tmdb.data.TmdbAuthLocalDataSource
import cinescout.auth.tmdb.data.local.mapper.toDatabaseAccessToken
import cinescout.auth.tmdb.data.model.TmdbAccessToken
import cinescout.database.TmdbCredentialsQueries

class RealTmdbAuthLocalDataSource(
    private val credentialsQueries: TmdbCredentialsQueries
) : TmdbAuthLocalDataSource {

    override suspend fun storeAccessToken(accessToken: TmdbAccessToken) {
        credentialsQueries.insertCredentials(accessToken.toDatabaseAccessToken())
    }
}
