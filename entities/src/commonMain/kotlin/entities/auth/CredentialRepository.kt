package entities.auth

import entities.TmdbStringId
import kotlinx.coroutines.flow.Flow

interface CredentialRepository {

    fun findTmdbAccessTokenBlocking(): String?
    fun findTmdbAccountId(): Flow<TmdbStringId?>
    fun findTmdbAccountIdBlocking(): TmdbStringId?
    suspend fun storeTmdbCredentials(accountId: TmdbStringId, token: String)
    suspend fun deleteTmdbAccessToken()
}
