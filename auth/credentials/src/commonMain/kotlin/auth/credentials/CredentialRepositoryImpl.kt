package auth.credentials

import database.credentials.TmdbCredentialQueries
import database.suspendAsOneOrNull
import entities.TmdbStringId
import entities.auth.CredentialRepository

internal class CredentialRepositoryImpl(
    private val tmdbCredentials: TmdbCredentialQueries
) : CredentialRepository {

    private var cachedAccountId: TmdbStringId? = null
    private var cachedTmdbAccessToken: String? = null

    override suspend fun findTmdbAccessTokenBlocking(): String? =
        cachedTmdbAccessToken
            ?: tmdbCredentials.selectAccessToken().suspendAsOneOrNull()

    override suspend fun findTmdbAccountIdBlocking(): TmdbStringId? =
        cachedAccountId
            ?: tmdbCredentials.selectAccountId().suspendAsOneOrNull()

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
