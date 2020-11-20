package domain.stats

import entities.Either
import entities.left
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SyncTmdbStats {

    operator fun invoke(): Flow<Either<Error, State>> =
        // TODO
        flowOf(Error.left())

    sealed class State {
        object Loading : State()
        object Completed : State()
    }

    object Error : entities.Error
}

typealias Either_SyncTmdbStats = Either<SyncTmdbStats.Error, SyncTmdbStats.State>
