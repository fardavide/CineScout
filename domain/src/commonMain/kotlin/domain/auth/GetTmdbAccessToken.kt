package domain.auth

import entities.auth.CredentialRepository

class GetTmdbAccessToken(
    private val credentials: CredentialRepository
) {

    fun blocking(): String? =
        credentials.findTmdbAccessTokenBlocking()
}
