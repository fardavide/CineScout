package entities.auth

import entities.TmdbStringId

interface CredentialRepository {

    fun findTmdbAccessTokenBlocking(): String?
    fun findTmdbAccountIdBlocking(): TmdbStringId?
    suspend fun storeTmdbCredentials(accountId: TmdbStringId, token: String)
    suspend fun deleteTmdbAccessToken()
}
