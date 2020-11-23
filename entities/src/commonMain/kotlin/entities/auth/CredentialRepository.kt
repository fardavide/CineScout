package entities.auth

import entities.TmdbId
import entities.TmdbStringId
import kotlinx.coroutines.flow.Flow

interface CredentialRepository {

    // Tmdb
    fun findTmdbAccessTokenBlocking(): String?
    fun findTmdbV3accountId(): Flow<TmdbId?>
    fun findTmdbV4accountId(): Flow<TmdbStringId?>
    fun findTmdbV3accountIdBlocking(): TmdbId?
    fun findTmdbV4accountIdBlocking(): TmdbStringId?
    fun findTmdbSessionIdBlocking(): String?
    suspend fun storeTmdbCredentials(v4accountId: TmdbStringId, token: String, sessionId: String)
    suspend fun storeTmdbV3AccountId(id: TmdbId)
    suspend fun deleteTmdbAccessToken()

    // Trakt
    fun findTraktAccessTokenBlocking(): String?
    suspend fun storeTraktAccessToken(token: String)
    suspend fun deleteTraktAccessToken()
}
