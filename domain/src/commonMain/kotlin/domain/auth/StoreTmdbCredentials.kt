package domain.auth

import entities.TmdbStringId
import entities.auth.CredentialRepository

class StoreTmdbCredentials(
    private val credentials: CredentialRepository
) {

    suspend operator fun invoke(v4accountId: TmdbStringId, token: String, sessionId: String) {
        credentials.storeTmdbCredentials(v4accountId, token, sessionId)
    }
}
