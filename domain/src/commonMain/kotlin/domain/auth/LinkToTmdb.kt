package domain.auth

import entities.Either
import entities.auth.TmdbAuth
import entities.auth.TmdbAuth.LoginError
import entities.auth.TmdbAuth.LoginError.InputError.InvalidEmail
import entities.auth.TmdbAuth.LoginError.InputError.InvalidPassword
import entities.field.Either_EmailAddress
import entities.field.Either_Password
import entities.or

/**
 * Link the application to Tmdb source and run a sync local -> server and then sever -> local
 */
class LinkToTmdb(
    private val auth: TmdbAuth
) {

    suspend operator fun invoke(
        email: Either_EmailAddress,
        password: Either_Password
    ): Either<LoginError, Unit> = Either.fix {
        val (validatedEmail) = email or ::InvalidEmail
        val (validatedPassword) = password or ::InvalidPassword
        auth.login(validatedEmail, validatedPassword)
    }
}
