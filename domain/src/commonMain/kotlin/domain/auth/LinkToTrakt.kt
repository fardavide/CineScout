package domain.auth

import domain.auth.Link.Error
import domain.auth.Link.State
import domain.stats.SyncTraktStats
import entities.auth.TmdbAuth
import entities.foldMap
import entities.then
import kotlinx.coroutines.flow.Flow

/**
 * Link the application to Tmdb source and run a sync local -> server and then sever -> local
 */
class LinkToTrakt(
    private val auth: TmdbAuth,
    private val syncStats: SyncTraktStats
) {

    operator fun invoke(): Flow<Either_LinkResult> =
        login() then ::sync

    private fun login(): Flow<Either_LinkResult> =
        auth.login()
            .foldMap(Error::Login, State::Login)

    private fun sync(): Flow<Either_LinkResult> =
        syncStats()
            .foldMap(Error::Sync, State::Sync)
}
