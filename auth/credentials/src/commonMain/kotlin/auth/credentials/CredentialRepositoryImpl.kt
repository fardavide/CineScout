package auth.credentials

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import database.credentials.TmdbCredentialQueries
import entities.TmdbId
import entities.TmdbStringId
import entities.auth.CredentialRepository
import kotlinx.coroutines.flow.Flow

internal class CredentialRepositoryImpl(
    private val tmdbCredentials: TmdbCredentialQueries
) : CredentialRepository {

    private var cachedV3accountId: TmdbId? = null
    private var cachedV4accountId: TmdbStringId? = null
    private var cachedSessionId: String? = null
    private var cachedTmdbAccessToken: String? = null

    override fun findTmdbAccessTokenBlocking(): String? =
        cachedTmdbAccessToken
            ?: tmdbCredentials.selectAccessToken().executeAsOneOrNull()

    override fun findTmdbV3accountId(): Flow<TmdbId?> =
        tmdbCredentials.selectV3accountId().asFlow().mapToOneOrNull()

    override fun findTmdbV4accountId(): Flow<TmdbStringId?> =
        tmdbCredentials.selectV4accountId().asFlow().mapToOneOrNull()

    override fun findTmdbV3accountIdBlocking(): TmdbId? =
        cachedV3accountId
            ?: tmdbCredentials.selectV3accountId().executeAsOneOrNull()

    override fun findTmdbV4accountIdBlocking(): TmdbStringId? =
        cachedV4accountId
            ?: tmdbCredentials.selectV4accountId().executeAsOneOrNull()

    override fun findTmdbSessionIdBlocking(): String? =
        cachedSessionId
            ?: tmdbCredentials.selectSessionId().executeAsOneOrNull()

    override suspend fun storeTmdbCredentials(v4accountId: TmdbStringId, token: String, sessionId: String) {
        cachedV4accountId = v4accountId
        cachedSessionId = sessionId
        cachedTmdbAccessToken = token
        tmdbCredentials.insert(v4accountId, token, sessionId)
    }

    override suspend fun storeTmdbV3AccountId(id: TmdbId) {
        cachedV3accountId = id
        tmdbCredentials.insertV3accountId(id)
    }

    override suspend fun deleteTmdbAccessToken() {
        cachedTmdbAccessToken = null
        tmdbCredentials.delete()
    }
}
