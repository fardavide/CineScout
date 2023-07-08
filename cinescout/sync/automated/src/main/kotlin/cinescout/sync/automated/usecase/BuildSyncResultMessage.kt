package cinescout.sync.automated.usecase

import android.content.Context
import cinescout.resources.R.plurals
import cinescout.resources.R.string
import cinescout.resources.string
import cinescout.sync.automated.model.SyncResult
import cinescout.utils.android.NetworkErrorToMessageMapper
import org.koin.core.annotation.Factory

@Factory
internal class BuildSyncResultMessage(
    private val context: Context,
    private val errorMapper: NetworkErrorToMessageMapper
) {

    operator fun invoke(
        fetchScreenplaysResult: SyncResult<Int>,
        syncHistoryResult: SyncResult<Unit>,
        syncRatingsResult: SyncResult<Unit>,
        syncWatchlistResult: SyncResult<Unit>
    ): String = context.getString(
        string.sync_result_notification_content,
        syncHistoryResult.string(),
        syncWatchlistResult.string(),
        syncRatingsResult.string(),
        fetchScreenplaysResult.string()
    )

    @JvmName("string_Unit")
    private fun SyncResult<Unit>.string(): String = when (this) {
        is SyncResult.Error -> context.string(errorMapper.toMessage(this.networkError))
        SyncResult.Skipped -> context.getString(string.sync_result_skipped)
        is SyncResult.Success -> context.getString(string.sync_result_success)
    }

    @JvmName("string_Int")
    private fun SyncResult<Int>.string(): String = when (this) {
        is SyncResult.Error -> context.string(errorMapper.toMessage(this.networkError))
        SyncResult.Skipped -> context.getString(string.sync_result_skipped)
        is SyncResult.Success -> context.resources.getQuantityString(plurals.sync_result_success_int, value, value)
    }
}
