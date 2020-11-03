package domain.stats

import entities.Either
import kotlinx.coroutines.flow.Flow

class SyncTmdbStats {

    operator fun invoke(): Flow<Either<Error, State>> {
        TODO("run sync")
    }

    sealed class State {
        object Loading : State()
        object Completed : State()
    }

    object Error : entities.Error
}

typealias Either_SyncTmdbStats = Either<SyncTmdbStats.Error, SyncTmdbStats.State>

class LaunchSyncTmdbStats(
    val syncTmdbStats: SyncTmdbStats
) {

    operator fun invoke(): Flow<Either<SyncTmdbStats.Error, SyncTmdbStats.State>> =
        syncTmdbStats()
}
