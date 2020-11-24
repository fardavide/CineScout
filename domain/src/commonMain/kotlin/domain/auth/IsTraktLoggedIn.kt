package domain.auth

import entities.auth.CredentialRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class IsTraktLoggedIn(
    private val credentials: CredentialRepository
) {

    operator fun invoke(): Flow<Boolean> =
        credentials.findTraktAccessToken().map { it.isRight() }
}
