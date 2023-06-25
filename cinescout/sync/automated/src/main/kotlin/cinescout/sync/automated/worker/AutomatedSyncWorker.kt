package cinescout.sync.automated.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import arrow.core.right
import cinescout.rating.domain.usecase.SyncRatings
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.sync.domain.model.RequiredSync
import cinescout.sync.domain.model.SyncNotRequired
import cinescout.sync.domain.model.SyncRatingsKey
import cinescout.sync.domain.model.SyncWatchlistKey
import cinescout.sync.domain.usecase.GetRatingsSyncStatus
import cinescout.sync.domain.usecase.GetWatchlistSyncStatus
import cinescout.utils.android.createOutput
import cinescout.watchlist.domain.usecase.SyncWatchlist
import co.touchlab.kermit.Logger
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.koin.android.annotation.KoinWorker
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

@KoinWorker
internal class AutomatedSyncWorker(
    appContext: Context,
    params: WorkerParameters,
    private val getRatingsSyncStatus: GetRatingsSyncStatus,
    private val getWatchlistSyncStatus: GetWatchlistSyncStatus,
    private val syncRatings: SyncRatings,
    private val syncWatchlist: SyncWatchlist
) : CoroutineWorker(appContext, params) {

    private val logger = Logger.withTag("AutomatedSyncWorker")

    override suspend fun doWork(): Result = coroutineScope {
        logger.i("Starting automated sync.")

        val syncRatingsDeferred = async {
            when (getRatingsSyncStatus(SyncRatingsKey(ScreenplayTypeFilter.All))) {
                SyncNotRequired -> Unit.right()
                is RequiredSync -> syncRatings()
            }
        }
        val syncWatchlistDeferred = async {
            when (getWatchlistSyncStatus(SyncWatchlistKey(ScreenplayTypeFilter.All))) {
                SyncNotRequired -> Unit.right()
                is RequiredSync -> syncWatchlist()
            }
        }
        val syncRatingsResult = syncRatingsDeferred.await()
        val syncWatchlistResult = syncWatchlistDeferred.await()

        handleResults(
            didSyncRatingsSucceed = syncRatingsResult.isRight(),
            didSyncWatchlistSucceed = syncWatchlistResult.isRight()
        )
    }

    private fun handleResults(didSyncRatingsSucceed: Boolean, didSyncWatchlistSucceed: Boolean): Result =
        when {
            didSyncRatingsSucceed && didSyncWatchlistSucceed -> {
                logger.i("Successfully synced ratings and watchlist.")
                Result.success()
            }

            runAttemptCount < MaxAttempts -> {
                logger.w("Automated sync failed. Retrying...")
                Result.retry()
            }

            else -> {
                logger.e("Automated sync failed.")
                Result.failure(createOutput("Automated sync failed."))
            }
        }

    class Scheduler(private val workManager: WorkManager) {
        operator fun invoke() {
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
                Name,
                ExistingPeriodicWorkPolicy.UPDATE,
                request
            )
            Logger.withTag("AutomatedSyncWorker").i("Scheduled automated sync.")
        }
    }

    companion object {

        const val MaxAttempts = 5
        const val Name = "AutomatedSyncWorker"
        val Interval = 1.hours
        val FlexInterval = 30.minutes
    }
}
