package domain.auth

import domain.stats.Either_SyncTmdbStats
import domain.stats.LaunchSyncTmdbStats
import entities.Either
import entities.auth.TmdbAuth
import entities.auth.TmdbAuth.LoginError
import entities.auth.TmdbAuth.LoginError.InputError.InvalidEmail
import entities.auth.TmdbAuth.LoginError.InputError.InvalidPassword
import entities.field.Either_EmailAddress
import entities.field.Either_Password
import entities.or
import entities.right
import kotlinx.coroutines.flow.Flow

/**
 * Link the application to Tmdb source and run a sync local -> server and then sever -> local
 */
class LinkToTmdb(
    private val auth: TmdbAuth,
    private val launchSync: LaunchSyncTmdbStats
) {

    suspend operator fun invoke(
        email: Either_EmailAddress,
        password: Either_Password
    ): Either<LoginError, Flow<Either_SyncTmdbStats>> = Either.fix {
        val (validatedEmail) = email or ::InvalidEmail
        val (validatedPassword) = password or ::InvalidPassword
        auth.login(validatedEmail, validatedPassword).rightOrThrow()
        launchSync().right()
    }
}
