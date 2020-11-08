package domain.auth

import entities.auth.CredentialRepository

class GetTmdbAccountId(
    private val credentials: CredentialRepository
) {

    fun blocking(): String? =
        credentials.findTmdbAccessTokenBlocking()
}
