package domain.auth

import entities.auth.CredentialRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class IsTmdbLoggedIn(
    private val credentials: CredentialRepository
) {

    operator fun invoke(): Flow<Boolean> =
        credentials.findTmdbV3accountId().map { it != null }
}
