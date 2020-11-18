package domain.auth

import domain.stats.LaunchSyncTmdbStats
import domain.stats.SyncTmdbStats
import entities.Either
import entities.auth.TmdbAuth
import entities.foldMap
import entities.plus
import kotlinx.coroutines.flow.Flow

/**
 * Link the application to Tmdb source and run a sync local -> server and then sever -> local
 */
class LinkToTmdb(
    private val auth: TmdbAuth,
    private val launchSync: LaunchSyncTmdbStats
) {

    operator fun invoke(): Flow<Either<Error, State>> =
        login() + ::sync

    private fun login(): Flow<Either<Error, State>> =
        auth.login()
            .foldMap(Error::Login, State::Login)

    private fun sync(): Flow<Either<Error, State>> =
        launchSync()
            .foldMap(Error::Sync, State::Sync)


    sealed class State {
        object None : State()
        data class Login(val loginState: TmdbAuth.LoginState): State()
        data class Sync(val syncState: SyncTmdbStats.State): State()
    }

    sealed class Error {
        data class Login(val loginError: TmdbAuth.LoginError): Error()
        data class Sync(val syncError: SyncTmdbStats.Error): Error()
    }

}
