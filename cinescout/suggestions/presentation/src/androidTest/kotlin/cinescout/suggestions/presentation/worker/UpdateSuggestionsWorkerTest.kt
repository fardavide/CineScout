package cinescout.suggestions.presentation.worker

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.test.core.app.ApplicationProvider
import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.impl.utils.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import androidx.work.workDataOf
import arrow.core.left
import arrow.core.right
import cinescout.common.model.SuggestionError
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.usecase.UpdateSuggestedMovies
import cinescout.suggestions.presentation.usecase.BuildUpdateSuggestionsErrorNotification
import cinescout.suggestions.presentation.usecase.BuildUpdateSuggestionsForegroundNotification
import cinescout.suggestions.presentation.usecase.BuildUpdateSuggestionsSuccessNotification
import cinescout.suggestions.presentation.usecase.CreateUpdateSuggestionsGroup
import cinescout.utils.android.setInput
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.androidx.workmanager.factory.KoinWorkerFactory
import org.koin.core.context.startKoin
import org.koin.core.scope.Scope
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdateSuggestionsWorkerTest : AutoCloseKoinTest() {

    private lateinit var workManager: WorkManager
    private val Scope.createUpdateSuggestionsGroup get() = CreateUpdateSuggestionsGroup(
        context = get(),
        notificationManagerCompat = NotificationManagerCompat.from(get())
    )
    private val Scope.notificationManagerCompat get() = NotificationManagerCompat.from(get())
    private val updateSuggestedMovies: UpdateSuggestedMovies = mockk {
        coEvery { invoke(suggestionsMode = any()) } returns Unit.right()
    }
    private fun Scope.suggestionsWorker() = spyk(
        UpdateSuggestionsWorker(
            appContext = get(),
            params = get(),
            analytics = mockk(relaxed = true),
            buildUpdateSuggestionsErrorNotification = BuildUpdateSuggestionsErrorNotification(
                context = get(),
                notificationManagerCompat = notificationManagerCompat,
                createUpdateSuggestionsGroup = createUpdateSuggestionsGroup
            ),
            buildUpdateSuggestionsForegroundNotification = BuildUpdateSuggestionsForegroundNotification(
                context = get(),
                notificationManagerCompat = notificationManagerCompat,
                createUpdateSuggestionsGroup = createUpdateSuggestionsGroup
            ),
            buildUpdateSuggestionsSuccessNotification = BuildUpdateSuggestionsSuccessNotification(
                context = get(),
                notificationManagerCompat = notificationManagerCompat,
                createUpdateSuggestionsGroup = createUpdateSuggestionsGroup
            ),
            ioDispatcher = UnconfinedTestDispatcher(),
            notificationManagerCompat = notificationManagerCompat,
            updateSuggestedMovies = get()
        )
    ) {
        every { @Suppress("DEPRECATION") coroutineContext } returns
            UnconfinedTestDispatcher()
        every { runAttemptCount } returns Int.MAX_VALUE
    }

    @BeforeTest
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        // Koin
        startKoin {
            androidContext(context)
            modules(
                module {
                    factory { updateSuggestedMovies }
                    worker { suggestionsWorker() }
                }
            )
        }.koin

        // Work manager
        val workerFactory = DelegatingWorkerFactory()
            .apply {
                addFactory(KoinWorkerFactory())
            }

        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(workerFactory)
            .setExecutor(SynchronousExecutor())
            .setTaskExecutor(SynchronousExecutor())
            .build()

        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
        workManager = WorkManager.getInstance(context)
    }

    @Test
    fun failsWhenNoInput() {
        // given
        val expected = WorkInfo.State.FAILED

        // when
        val request = OneTimeWorkRequestBuilder<UpdateSuggestionsWorker>()
            .build()
        workManager.enqueue(request).result.get()

        // then
        val workInfo = workManager.getWorkInfoById(request.id).get()
        assertEquals(expected, workInfo.state)
    }

    @Test
    fun failsWhenWrongInput() {
        // given
        val expected = WorkInfo.State.FAILED

        // when
        val request = OneTimeWorkRequestBuilder<UpdateSuggestionsWorker>()
            .setInputData(workDataOf("key" to "value"))
            .build()
        workManager.enqueue(request).result.get()

        // then
        val workInfo = workManager.getWorkInfoById(request.id).get()
        assertEquals(expected, workInfo.state)
    }

    @Test
    fun succeedWhenRightInput() {
        // given
        val expected = WorkInfo.State.SUCCEEDED

        // when
        val request = OneTimeWorkRequestBuilder<UpdateSuggestionsWorker>()
            .setInput(SuggestionsMode.Deep)
            .build()
        workManager.enqueue(request).result.get()

        // then
        val workInfo = workManager.getWorkInfoById(request.id).get()
        assertEquals(expected, workInfo.state, message = workInfo.toString())
    }

    @Test
    fun failsWhenUpdateSuggestedMoviesFails() {
        // given
        val expected = WorkInfo.State.FAILED
        coEvery { updateSuggestedMovies(suggestionsMode = any()) } returns
            SuggestionError.NoSuggestions.left()

        // when
        val request = OneTimeWorkRequestBuilder<UpdateSuggestionsWorker>()
            .setInput(SuggestionsMode.Deep)
            .build()
        workManager.enqueue(request).result.get()

        // then
        val workInfo = workManager.getWorkInfoById(request.id).get()
        assertEquals(expected, workInfo.state, message = workInfo.toString())
    }
}
