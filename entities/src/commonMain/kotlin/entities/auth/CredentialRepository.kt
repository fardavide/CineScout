package entities.auth

import entities.TmdbId
import entities.TmdbStringId
import kotlinx.coroutines.flow.Flow

interface CredentialRepository {

    fun findTmdbAccessTokenBlocking(): String?
    fun findTmdbV3accountId(): Flow<TmdbId?>
    fun findTmdbV4accountId(): Flow<TmdbStringId?>
    fun findTmdbV3accountIdBlocking(): TmdbId?
    fun findTmdbV4accountIdBlocking(): TmdbStringId?
    fun findTmdbSessionIdBlocking(): String?
    suspend fun storeTmdbCredentials(v4accountId: TmdbStringId, token: String, sessionId: String)
    suspend fun storeTmdbV3AccountId(id: TmdbId)
    suspend fun deleteTmdbAccessToken()
}
