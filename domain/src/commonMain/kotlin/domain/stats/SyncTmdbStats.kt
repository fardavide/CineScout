package domain.stats

import entities.Either
import entities.Error
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SyncTmdbStats {

    suspend operator fun invoke(): Either<SyncError, Unit> {
        TODO("run sync")
    }

    object SyncError : Error
}

typealias Either_SyncTmdbStats = Either<SyncTmdbStats.SyncError, Unit>

class LaunchSyncTmdbStats(
    val syncTmdbStats: SyncTmdbStats
) {

    suspend operator fun invoke(): Flow<Either_SyncTmdbStats> =
        flowOf(syncTmdbStats())
}
