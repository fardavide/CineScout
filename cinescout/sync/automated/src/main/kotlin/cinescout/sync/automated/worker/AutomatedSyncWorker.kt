package cinescout.sync.automated.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import cinescout.history.domain.usecase.SyncHistory
import cinescout.notification.SyncNotifications
import cinescout.rating.domain.usecase.SyncRatings
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.sync.automated.model.SyncResult
import cinescout.sync.automated.model.toSyncResult
import cinescout.sync.automated.usecase.BuildSyncResultMessage
import cinescout.sync.domain.model.RequiredSync
import cinescout.sync.domain.model.SyncHistoryKey
import cinescout.sync.domain.model.SyncNotRequired
import cinescout.sync.domain.model.SyncRatingsKey
import cinescout.sync.domain.model.SyncWatchlistKey
import cinescout.sync.domain.usecase.FetchScreenplays
import cinescout.sync.domain.usecase.GetHistorySyncStatus
import cinescout.sync.domain.usecase.GetRatingsSyncStatus
import cinescout.sync.domain.usecase.GetWatchlistSyncStatus
import cinescout.utils.android.createOutput
import cinescout.watchlist.domain.usecase.SyncWatchlist
import co.touchlab.kermit.Logger
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.koin.android.annotation.KoinWorker
import kotlin.time.Duration.Companion.hours
import kotlin.time.toJavaDuration

@KoinWorker
internal class AutomatedSyncWorker(
    appContext: Context,
    params: WorkerParameters,
    private val buildSyncResultMessage: BuildSyncResultMessage,
    private val fetchScreenplays: FetchScreenplays,
    private val getHistorySyncStatus: GetHistorySyncStatus,
    private val getRatingsSyncStatus: GetRatingsSyncStatus,
    private val getWatchlistSyncStatus: GetWatchlistSyncStatus,
    private val notifications: SyncNotifications,
    private val syncHistory: SyncHistory,
    private val syncRatings: SyncRatings,
    private val syncWatchlist: SyncWatchlist
) : CoroutineWorker(appContext, params) {

    private val logger = Logger.withTag("AutomatedSyncWorker")

    override suspend fun doWork(): Result = coroutineScope {
        logger.i("Starting automated sync.")
        setForeground(notifications.foregroundInfo())

        val syncHistoryDeferred = async {
            when (getHistorySyncStatus(SyncHistoryKey(ScreenplayTypeFilter.All))) {
                SyncNotRequired -> SyncResult.Skipped
                is RequiredSync -> syncHistory().toSyncResult()
            }
        }
        val syncRatingsDeferred = async {
            when (getRatingsSyncStatus(SyncRatingsKey(ScreenplayTypeFilter.All))) {
                SyncNotRequired -> SyncResult.Skipped
                is RequiredSync -> syncRatings().toSyncResult()
            }
        }
        val syncWatchlistDeferred = async {
            when (getWatchlistSyncStatus(SyncWatchlistKey(ScreenplayTypeFilter.All))) {
                SyncNotRequired -> SyncResult.Skipped
                is RequiredSync -> syncWatchlist().toSyncResult()
            }
        }
        val syncHistoryResult = syncHistoryDeferred.await()
        val syncRatingsResult = syncRatingsDeferred.await()
        val syncWatchlistResult = syncWatchlistDeferred.await()

        val fetchScreenplaysResult = fetchScreenplays().toSyncResult()

        handleResults(
            fetchScreenplaysResult = fetchScreenplaysResult,
            syncHistoryResult = syncHistoryResult,
            syncRatingsResult = syncRatingsResult,
            syncWatchlistResult = syncWatchlistResult
        )
    }

    private fun handleResults(
        fetchScreenplaysResult: SyncResult<Int>,
        syncHistoryResult: SyncResult<Unit>,
        syncRatingsResult: SyncResult<Unit>,
        syncWatchlistResult: SyncResult<Unit>
    ): Result {
        val didAllSucceed = listOf(
            fetchScreenplaysResult,
            syncHistoryResult,
            syncRatingsResult,
            syncWatchlistResult
        ).none { it is SyncResult.Error }

        val logMessage = "Sync history: $syncHistoryResult " +
            "Sync ratings: $syncRatingsResult " +
            "Sync watchlist: $syncWatchlistResult " +
            "Fetch screenplays: $fetchScreenplaysResult"
        val notificationMessage = buildSyncResultMessage(
            fetchScreenplaysResult = fetchScreenplaysResult,
            syncHistoryResult = syncHistoryResult,
            syncRatingsResult = syncRatingsResult,
            syncWatchlistResult = syncWatchlistResult
        )

        return when {
            didAllSucceed -> {
                notifications.success(notificationMessage).show()
                logger.i(logMessage)
                Result.success()
            }

            runAttemptCount < MaxAttempts -> {
                logger.w(logMessage)
                Result.retry()
            }

            else -> {
                logger.e(logMessage)
                notifications.error(notificationMessage).show()
                Result.failure(createOutput(logMessage))
            }
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo = notifications.foregroundInfo()

    class Scheduler(private val workManager: WorkManager) {

        fun scheduleExpedited() {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request = OneTimeWorkRequestBuilder<AutomatedSyncWorker>()
                .setConstraints(constraints)
                .setExpedited(OutOfQuotaPolicy.DROP_WORK_REQUEST)
                .build()

            workManager.enqueueUniqueWork(
                ExpeditedName,
                ExistingWorkPolicy.KEEP,
                request
            )
            Logger.withTag("AutomatedSyncWorker").i("Scheduled expedited automated sync.")
        }

        fun schedulePeriodic() {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val requestBuilder = PeriodicWorkRequestBuilder<AutomatedSyncWorker>(
                repeatInterval = Interval.toJavaDuration(),
                flexTimeInterval = FlexInterval.toJavaDuration()
            )

            val request = requestBuilder
                .setConstraints(constraints)
                .build()

            workManager.enqueueUniquePeriodicWork(
                PeriodicName,
                ExistingPeriodicWorkPolicy.UPDATE,
                request
            )
            Logger.withTag("AutomatedSyncWorker").i("Scheduled periodic automated sync.")
        }
    }

    companion object {

        const val MaxAttempts = 3
        const val ExpeditedName = "AutomatedSyncWorker_Expedited"
        const val PeriodicName = "AutomatedSyncWorker_Periodic"
        val Interval = 3.hours
        val FlexInterval = 1.hours
    }
}
