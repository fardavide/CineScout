package domain.auth

import entities.BlankStringError
import entities.Either
import entities.EmailAddress
import entities.NotBlankString
import entities.RegexMismatchError
import entities.auth.TmdbAuth
import entities.then

/**
 * Link the application to Tmdb source and run a sync local -> server and then sever -> local
 */
class LinkToTmdb(
    private val auth: TmdbAuth
) {

    suspend operator fun invoke(
        email: Either<RegexMismatchError, EmailAddress>,
        password: Either<BlankStringError, NotBlankString>
    ): Either<TmdbAuth.LoginError, Unit> {
        email then password
        val (a, b) = email
        return auth.login(email, password)
    }
}
