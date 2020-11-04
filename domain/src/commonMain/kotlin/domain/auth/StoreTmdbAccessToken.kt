package domain.auth

import entities.auth.CredentialRepository

class StoreTmdbAccessToken(
    private val credentials: CredentialRepository
) {

    suspend operator fun invoke(token: String) {
        credentials.storeTmdbAccessToken(token)
    }
}
