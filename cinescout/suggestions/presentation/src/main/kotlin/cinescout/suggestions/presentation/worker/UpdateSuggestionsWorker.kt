package cinescout.suggestions.presentation.worker

import android.annotation.SuppressLint
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
import arrow.core.Either
import arrow.core.left
import cinescout.error.NetworkError
import cinescout.notification.UpdateSuggestionsNotifications
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.usecase.UpdateSuggestions
import cinescout.utils.android.createOutput
import cinescout.utils.android.requireInput
import cinescout.utils.android.setInput
import cinescout.utils.kotlin.IoDispatcher
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import org.koin.android.annotation.KoinWorker
import org.koin.core.annotation.Named
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@KoinWorker
class UpdateSuggestionsWorker(
    appContext: Context,
    params: WorkerParameters,
    @Named(IoDispatcher) private val ioDispatcher: CoroutineDispatcher,
    private val notifications: UpdateSuggestionsNotifications,
    private val updateSuggestions: UpdateSuggestions
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        val input = requireInput<SuggestionsMode>()
        setForeground(notifications.foregroundInfo())
        val result = withTimeoutOrNull(10.minutes) {
            updateSuggestions(ScreenplayTypeFilter.All, input)
        } ?: NetworkError.Unknown.left()
        handleResult(input, result)
        return@withContext toWorkerResult(result)
    }

    @SuppressLint("MissingPermission")
    private fun handleResult(input: SuggestionsMode, result: Either<NetworkError, Unit>) {
        result
            .onRight {
                Logger.i("Successfully updated suggestions for ${input.name}")
                notifications.success().show()
            }
            .onLeft { error ->
                Logger.e("Error updating suggestions for ${input.name}: $error")
                notifications.error().show()
            }
    }

    private fun toWorkerResult(result: Either<NetworkError, Unit>): Result = result.fold(
        ifRight = { Result.success() },
        ifLeft = { error ->
            when {
                runAttemptCount < MaxAttempts -> Result.retry()
                else -> Result.failure(createOutput(error.toString()))
            }
        }
    )

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
        val Interval = 1.days
        val FlexInterval = 3.days
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
