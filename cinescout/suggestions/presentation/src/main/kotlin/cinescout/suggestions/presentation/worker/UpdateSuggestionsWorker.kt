package cinescout.suggestions.presentation.worker

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.usecase.UpdateSuggestedMovies
import cinescout.utils.android.createOutput
import cinescout.utils.android.requireInput
import cinescout.utils.android.setInput
import co.touchlab.kermit.Logger
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds
import kotlin.time.measureTimedValue
import kotlin.time.toJavaDuration

class UpdateSuggestionsWorker(
    appContext: Context,
    params: WorkerParameters,
    private val analytics: FirebaseAnalytics,
    private val ioDispatcher: CoroutineDispatcher,
    private val updateSuggestedMovies: UpdateSuggestedMovies
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        val input = requireInput<SuggestionsMode>()
        val (result, time) = measureTimedValue { updateSuggestedMovies(input) }
        analytics.logEvent(Analytics.EventName) {
            param(Analytics.TypeParameter, input.name)
            param(Analytics.TimeParameter, time.inWholeSeconds)
            param(
                Analytics.ResultParameter,
                result.fold(ifLeft = { "${Analytics.ErrorWithReason}$it" }, ifRight = { Analytics.Success })
            )
        }
        result.fold(
            ifRight = {
                Logger.i("Successfully updated suggestions for ${input.name}")
                Result.success()
            },
            ifLeft = { error ->
                when {
                    runAttemptCount < MaxAttempts -> {
                        Logger.e("Error updating suggestions for ${input.name}: $error")
                        Result.retry()
                    }
                    else -> {
                        Logger.e("Error updating suggestions for ${input.name}: $error")
                        Result.failure(createOutput(error.toString()))
                    }
                }
            }
        )
    }

    class Scheduler(private val workManager: WorkManager) {

        operator fun invoke(suggestionsMode: SuggestionsMode) {
            when (suggestionsMode) {
                SuggestionsMode.Deep -> schedulePeriodic()
                SuggestionsMode.Quick -> scheduleExpedited()
            }
        }

        private fun scheduleExpedited() {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val requestBuilder = OneTimeWorkRequestBuilder<UpdateSuggestionsWorker>()

            val request = requestBuilder
                .setConstraints(constraints)
                .setInput(SuggestionsMode.Quick)
                .setBackoffCriteria(BackoffPolicy.LINEAR, Backoff.toJavaDuration())
                .setInitialRunAttemptCount(MaxAttempts)
                // TODO needs notification
                // .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()

            workManager.enqueueUniqueWork(
                ExpeditedName,
                ExistingWorkPolicy.REPLACE,
                request
            )
        }

        private fun schedulePeriodic() {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresDeviceIdle(true)
                .build()

            val requestBuilder = PeriodicWorkRequestBuilder<UpdateSuggestionsWorker>(
                repeatInterval = Interval.toJavaDuration(),
                flexTimeInterval = FlexInterval.toJavaDuration()
            )

            val request = requestBuilder
                .setConstraints(constraints)
                .setInput(SuggestionsMode.Deep)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, Backoff.toJavaDuration())
                .setInitialRunAttemptCount(MaxAttempts)
                .build()

            workManager.enqueueUniquePeriodicWork(
                PeriodicName,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }

    companion object {

        const val MaxAttempts = 5
        const val ExpeditedName = "UpdateSuggestionsWorker.expedited"
        const val PeriodicName = "UpdateSuggestionsWorker.periodic"
        val Backoff = 10.seconds
        val Interval = 1.hours
        val FlexInterval = 3.hours
    }

    private object Analytics {

        const val ErrorWithReason = "Error: "
        const val EventName = "update_suggestions"
        const val ResultParameter = "result"
        const val Success = "Success"
        const val TimeParameter = "time_seconds"
        const val TypeParameter = "type"
    }
}
