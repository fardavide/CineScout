package domain.auth

import entities.auth.CredentialRepository

class GetTmdbSessionId(
    private val credentials: CredentialRepository
) {

    fun blocking(): String? =
        credentials.findTmdbSessionIdBlocking()
}
