package domain.stats

import entities.Either

interface Sync {

    sealed class State {
        object Loading : State()
        object Completed : State()
    }

    object Error : entities.Error
}

typealias Either_SyncResult = Either<Sync.Error, Sync.State>

