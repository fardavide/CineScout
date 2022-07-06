package cinescout.auth.tmdb.data

import cinescout.auth.tmdb.data.model.TmdbAccessToken

interface TmdbAuthLocalDataSource {

    suspend fun storeAccessToken(accessToken: TmdbAccessToken)
}
