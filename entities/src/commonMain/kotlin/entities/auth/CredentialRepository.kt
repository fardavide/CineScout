package entities.auth

interface CredentialRepository {

    fun findTmdbAccessTokenBlocking(): String?
    suspend fun storeTmdbAccessToken(token: String)
    suspend fun deleteTmdbAccessToken()
}
