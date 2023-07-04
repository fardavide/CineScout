package cinescout.sync.automated.model

import arrow.core.Either
import cinescout.error.NetworkError

internal sealed interface SyncResult {

    @JvmInline
    value class Error(val networkError: NetworkError) : SyncResult
    object Skipped : SyncResult
    object Success : SyncResult
}

internal fun Either<NetworkError, Unit>.toSyncResult(): SyncResult =
    fold(ifLeft = SyncResult::Error, ifRight = { SyncResult.Success })
