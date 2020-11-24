package auth.credentials

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import database.asFlowOfOneOrError
import database.credentials.TmdbCredentialQueries
import database.credentials.TraktCredentialQueries
import entities.Either
import entities.MissingCache
import entities.TmdbId
import entities.TmdbStringId
import entities.auth.CredentialRepository
import kotlinx.coroutines.flow.Flow

internal class CredentialRepositoryImpl(
    private val tmdbCredentials: TmdbCredentialQueries,
    private val traktCredentials: TraktCredentialQueries
) : CredentialRepository {

    // Tmdb
    private var cachedTmdbV3accountId: TmdbId? = null
    private var cachedTmdbV4accountId: TmdbStringId? = null
    private var cachedTmdbSessionId: String? = null
    private var cachedTmdbAccessToken: String? = null

    // Trakt
    private var cachedTraktAccessToken: String? = null

    // Tmdb
    override fun findTmdbAccessTokenBlocking(): String? =
        cachedTmdbAccessToken
            ?: tmdbCredentials.selectAccessToken().executeAsOneOrNull()

    override fun findTmdbV3accountId(): Flow<TmdbId?> =
        tmdbCredentials.selectV3accountId().asFlow().mapToOneOrNull()

    override fun findTmdbV4accountId(): Flow<TmdbStringId?> =
        tmdbCredentials.selectV4accountId().asFlow().mapToOneOrNull()

    override fun findTmdbV3accountIdBlocking(): TmdbId? =
        cachedTmdbV3accountId
            ?: tmdbCredentials.selectV3accountId().executeAsOneOrNull()

    override fun findTmdbV4accountIdBlocking(): TmdbStringId? =
        cachedTmdbV4accountId
            ?: tmdbCredentials.selectV4accountId().executeAsOneOrNull()

    override fun findTmdbSessionIdBlocking(): String? =
        cachedTmdbSessionId
            ?: tmdbCredentials.selectSessionId().executeAsOneOrNull()

    override suspend fun storeTmdbCredentials(v4accountId: TmdbStringId, token: String, sessionId: String) {
        cachedTmdbV4accountId = v4accountId
        cachedTmdbSessionId = sessionId
        cachedTmdbAccessToken = token
        tmdbCredentials.insert(v4accountId, token, sessionId)
    }

    override suspend fun storeTmdbV3AccountId(id: TmdbId) {
        cachedTmdbV3accountId = id
        tmdbCredentials.insertV3accountId(id)
    }

    override suspend fun deleteTmdbAccessToken() {
        cachedTmdbAccessToken = null
        tmdbCredentials.delete()
    }

    override fun findTraktAccessToken(): Flow<Either<MissingCache, String>> =
        traktCredentials.selectAccessToken().asFlowOfOneOrError()

    override fun findTraktAccessTokenBlocking(): String? =
        cachedTraktAccessToken
            ?: traktCredentials.selectAccessToken().executeAsOneOrNull()

    override suspend fun storeTraktAccessToken(token: String) {
        cachedTraktAccessToken = token
        traktCredentials.insert(token)
    }

    override suspend fun deleteTraktAccessToken() {
        cachedTraktAccessToken = null
        traktCredentials.delete()
    }
}
