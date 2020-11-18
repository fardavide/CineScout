package entities.auth

import entities.TmdbStringId

interface CredentialRepository {

    suspend fun findTmdbAccessTokenBlocking(): String?
    suspend fun findTmdbAccountIdBlocking(): TmdbStringId?
    suspend fun storeTmdbCredentials(accountId: TmdbStringId, token: String)
    suspend fun deleteTmdbAccessToken()
}
