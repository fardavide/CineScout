package domain.auth

import entities.TmdbId
import entities.TmdbStringId
import entities.auth.CredentialRepository

class GetTmdbAccountId(
    private val credentials: CredentialRepository
) {

    fun blocking(): TmdbStringId? =
        credentials.findTmdbAccountIdBlocking()
}
