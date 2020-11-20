package domain.auth

import entities.TmdbId
import entities.auth.CredentialRepository

class StoreTmdbAccountId(
    private val credentials: CredentialRepository
) {

    suspend operator fun invoke(id: TmdbId) {
        credentials.storeTmdbV3AccountId(id)
    }
}
