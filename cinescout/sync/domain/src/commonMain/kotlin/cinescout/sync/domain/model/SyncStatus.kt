package cinescout.sync.domain.model

sealed interface SyncStatus

object SyncNotRequired : SyncStatus

sealed interface RequiredSync : SyncStatus {

    object Initial : RequiredSync
    object Complete : RequiredSync
}
