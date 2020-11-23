package domain.auth

import entities.auth.CredentialRepository

class StoreTraktAccessToken(
    private val credentials: CredentialRepository
) {

    suspend operator fun invoke(token: String) {
        credentials.storeTraktAccessToken(token)
    }
}
