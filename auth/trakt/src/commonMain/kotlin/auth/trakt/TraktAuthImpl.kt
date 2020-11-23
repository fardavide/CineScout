package auth.trakt

import entities.auth.Either_LoginResult
import entities.auth.TraktAuth
import kotlinx.coroutines.flow.Flow

internal class TraktAuthImpl(
    private val authService: AuthService
) : TraktAuth {

    override fun login(): Flow<Either_LoginResult> =
        authService.login()
}
