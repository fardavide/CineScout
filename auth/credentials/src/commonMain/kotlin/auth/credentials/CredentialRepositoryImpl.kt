package auth.credentials

import database.credentials.TmdbCredentialQueries
import entities.TmdbStringId
import entities.auth.CredentialRepository

internal class CredentialRepositoryImpl(
    private val tmdbCredentials: TmdbCredentialQueries
) : CredentialRepository {

    private var cachedAccountId: TmdbStringId? = null
    private var cachedTmdbAccessToken: String? = null

    override fun findTmdbAccessTokenBlocking(): String? =
        cachedTmdbAccessToken
            ?: tmdbCredentials.selectAccessToken().executeAsOneOrNull()

    override fun findTmdbAccountIdBlocking(): TmdbStringId? =
        cachedAccountId
            ?: tmdbCredentials.selectAccountId().executeAsOneOrNull()

    override suspend fun storeTmdbCredentials(accountId: TmdbStringId, token: String) {
        cachedAccountId = accountId
        cachedTmdbAccessToken = token
        tmdbCredentials.insert(accountId, token)
    }

    override suspend fun deleteTmdbAccessToken() {
        cachedTmdbAccessToken = null
        tmdbCredentials.delete()
    }
}
