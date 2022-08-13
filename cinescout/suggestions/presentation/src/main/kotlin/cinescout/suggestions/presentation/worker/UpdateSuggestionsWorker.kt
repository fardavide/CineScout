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
import cinescout.movies.domain.model.SuggestionError
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.usecase.UpdateSuggestedMovies
import cinescout.utils.android.createOutput
import cinescout.utils.android.requireInput
import cinescout.utils.android.setInput
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

class UpdateSuggestionsWorker(
    appContext: Context,
    params: WorkerParameters,
    private val ioDispatcher: CoroutineDispatcher,
    private val updateSuggestedMovies: UpdateSuggestedMovies
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        val input = requireInput<SuggestionsMode>()
        val result = updateSuggestedMovies(input)
        result.fold(
            ifRight = { Result.success() },
            ifLeft = { error ->
                when (error) {
                    SuggestionError.NoSuggestions -> Result.failure(createOutput(error.toString()))
                    is SuggestionError.Source -> Result.retry()
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
                .setRequiresCharging(true)
                .build()

            val requestBuilder = PeriodicWorkRequestBuilder<UpdateSuggestionsWorker>(
                repeatInterval = Interval.toJavaDuration(),
                flexTimeInterval = FlexInterval.toJavaDuration()
            )

            val request = requestBuilder
                .setConstraints(constraints)
                .setInput(SuggestionsMode.Deep)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, Backoff.toJavaDuration())
                .build()

            workManager.enqueueUniquePeriodicWork(
                PeriodicName,
                ExistingPeriodicWorkPolicy.REPLACE,
                request
            )
        }
    }

    companion object {

        const val ExpeditedName = "UpdateSuggestionsWorker.expedited"
        const val PeriodicName = "UpdateSuggestionsWorker.periodic"
        val Backoff = 10.seconds
        val Interval = 6.hours
        val FlexInterval = 3.hours
    }
}
