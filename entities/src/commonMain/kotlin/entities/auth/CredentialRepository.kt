package entities.auth

import entities.TmdbStringId
import kotlinx.coroutines.flow.Flow

interface CredentialRepository {

    fun findTmdbAccessTokenBlocking(): String?
    fun findTmdbAccountId(): Flow<TmdbStringId?>
    fun findTmdbAccountIdBlocking(): TmdbStringId?
    fun findTmdbSessionIdBlocking(): String?
    suspend fun storeTmdbCredentials(accountId: TmdbStringId, token: String, sessionId: String)
    suspend fun deleteTmdbAccessToken()
}
