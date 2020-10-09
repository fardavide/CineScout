package domain.auth

import entities.Either
import entities.EmailAddress
import entities.Name
import entities.NotBlankString
import entities.auth.TmdbAuth

/**
 * Link the application to Tmdb source and run a sync local -> server and then sever -> local
 */
class LinkToTmdb(
    private val auth: TmdbAuth
) {

    suspend operator fun invoke(email: EmailAddress, password: NotBlankString): Either<TmdbAuth.LoginError, Unit> =
        auth.login(email, password)
}
