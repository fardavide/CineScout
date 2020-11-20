package domain.auth

import entities.TmdbId
import entities.auth.CredentialRepository

class GetTmdbV3accountId(
    private val credentials: CredentialRepository
) {

    fun blocking(): TmdbId? =
        credentials.findTmdbV3accountIdBlocking()
}
