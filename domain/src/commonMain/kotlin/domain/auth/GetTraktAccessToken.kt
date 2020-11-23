package domain.auth

import entities.auth.CredentialRepository

class GetTraktAccessToken(
    private val credentials: CredentialRepository
) {

    fun blocking(): String? =
        credentials.findTraktAccessTokenBlocking()
}
