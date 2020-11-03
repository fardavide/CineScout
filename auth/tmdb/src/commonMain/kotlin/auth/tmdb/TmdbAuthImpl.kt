package auth.tmdb

import auth.tmdb.auth.AuthService
import entities.Either
import entities.auth.TmdbAuth
import kotlinx.coroutines.flow.Flow

internal class TmdbAuthImpl(
    private val authService: AuthService
) : TmdbAuth {

    override fun login(): Flow<Either<TmdbAuth.LoginError, TmdbAuth.LoginState>> =
        authService.login()

}
