package domain.auth

import entities.TmdbStringId
import entities.auth.CredentialRepository

class GetTmdbV4accountId(
    private val credentials: CredentialRepository
) {

    fun blocking(): TmdbStringId? =
        credentials.findTmdbV4accountIdBlocking()
}
