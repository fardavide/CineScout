package cinescout.sync.automated.usecase

import android.content.Context
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
        fetchScreenplaysResult: SyncResult,
        syncHistoryResult: SyncResult,
        syncRatingsResult: SyncResult,
        syncWatchlistResult: SyncResult
    ): String = context.getString(
        string.sync_result_notification_content,
        syncHistoryResult.string(),
        syncWatchlistResult.string(),
        syncRatingsResult.string(),
        fetchScreenplaysResult.string()
    )

    private fun SyncResult.string(): String = when (this) {
        is SyncResult.Error -> context.string(errorMapper.toMessage(this.networkError))
        SyncResult.Skipped -> context.getString(string.sync_result_skipped)
        SyncResult.Success -> context.getString(string.sync_result_success)
    }
}
