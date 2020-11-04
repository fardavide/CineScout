package auth.credentials

import database.credentials.TmdbCredentialQueries
import entities.auth.CredentialRepository

internal class CredentialRepositoryImpl(
    private val tmdbCredentials: TmdbCredentialQueries
) : CredentialRepository {

    private var cachedTmdbAccessToken: String? = null

    override fun findTmdbAccessTokenBlocking(): String? =
        cachedTmdbAccessToken
            ?: tmdbCredentials.select().executeAsOneOrNull()

    override suspend fun storeTmdbAccessToken(token: String) {
        cachedTmdbAccessToken = token
        tmdbCredentials.insert(token)
    }

    override suspend fun deleteTmdbAccessToken() {
        cachedTmdbAccessToken = null
        tmdbCredentials.delete()
    }
}
