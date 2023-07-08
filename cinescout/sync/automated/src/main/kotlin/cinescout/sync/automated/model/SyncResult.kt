package cinescout.sync.automated.model

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.sync.domain.model.FetchScreenplaysResult

internal sealed interface SyncResult<out T : Any> {

    @JvmInline
    value class Error(val networkError: NetworkError) : SyncResult<Nothing>

    object Skipped : SyncResult<Nothing>

    @JvmInline
    value class Success<out T : Any>(val value: T) : SyncResult<T>
}

@JvmName("toSyncResult_Unit")
internal fun Either<NetworkError, Unit>.toSyncResult(): SyncResult<Unit> =
    fold(ifLeft = { SyncResult.Error(it) }, ifRight = { SyncResult.Success(Unit) })

@JvmName("toSyncResult_FetchScreenplaysResult")
internal fun Either<NetworkError, FetchScreenplaysResult>.toSyncResult(): SyncResult<Int> = fold(
    ifLeft = { SyncResult.Error(it) },
    ifRight = { result ->
        when (result.fetchedCount) {
            0 -> SyncResult.Skipped
            else -> SyncResult.Success(result.fetchedCount)
        }
    }
)
