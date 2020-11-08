package domain.auth

import entities.TmdbStringId
import entities.auth.CredentialRepository

class StoreTmdbCredentials(
    private val credentials: CredentialRepository
) {

    suspend operator fun invoke(accountId: TmdbStringId, token: String) {
        credentials.storeTmdbCredentials(accountId, token)
    }
}
