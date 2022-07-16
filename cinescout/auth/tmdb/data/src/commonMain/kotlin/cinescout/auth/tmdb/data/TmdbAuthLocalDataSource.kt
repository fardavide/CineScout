package cinescout.auth.tmdb.data

import cinescout.auth.tmdb.data.model.TmdbCredentials

interface TmdbAuthLocalDataSource {

    fun findCredentialsBlocking(): TmdbCredentials?

    suspend fun storeCredentials(credentials: TmdbCredentials)
}
