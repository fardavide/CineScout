package cinescout.auth.tmdb.data

import cinescout.auth.tmdb.data.model.TmdbAccessToken
import cinescout.auth.tmdb.data.model.TmdbCredentials

interface TmdbAuthLocalDataSource {

    suspend fun storeAccessToken(accessToken: TmdbAccessToken)

    suspend fun storeCredentials(credentials: TmdbCredentials)
}
