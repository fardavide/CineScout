package auth.credentials

import database.credentials.TmdbCredentialQueries
import entities.TmdbStringId
import entities.auth.CredentialRepository
import util.unsupported

internal class CredentialRepositoryImpl(
    private val tmdbCredentials: TmdbCredentialQueries
) : CredentialRepository {

    private var cachedAccountId: TmdbStringId? = null
    private var cachedTmdbAccessToken: String? = null

    override fun findTmdbAccessTokenBlocking(): String? =
        cachedTmdbAccessToken
            ?: tmdbCredentials.selectAccessToken().executeAsOneOrNull()

    override suspend fun storeTmdbCredentials(accountId: TmdbStringId, token: String) {
        cachedAccountId = accountId
        cachedTmdbAccessToken = token
        tmdbCredentials.insert(accountId, token)
    }

    @Deprecated("Store with accountId", ReplaceWith("storeTmdbCredentials(accountId, token)"), DeprecationLevel.ERROR)
    override suspend fun storeTmdbAccessToken(token: String) {
        unsupported
    }

    override suspend fun deleteTmdbAccessToken() {
        cachedTmdbAccessToken = null
        tmdbCredentials.delete()
    }
}
