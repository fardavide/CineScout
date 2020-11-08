package entities.auth

import entities.TmdbStringId

interface CredentialRepository {

    fun findTmdbAccessTokenBlocking(): String?
    suspend fun storeTmdbCredentials(accountId: TmdbStringId, token: String)
    @Deprecated("Store with accountId", ReplaceWith("storeTmdbCredentials(accountId, token)"), DeprecationLevel.ERROR)
    suspend fun storeTmdbAccessToken(token: String)
    suspend fun deleteTmdbAccessToken()
}
