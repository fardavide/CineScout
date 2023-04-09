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
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.impl.utils.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import androidx.work.workDataOf
import cinescout.error.NetworkError
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.usecase.FakeUpdateSuggestions
import cinescout.suggestions.domain.usecase.UpdateSuggestions
import cinescout.suggestions.presentation.SuggestionsPresentationModule
import cinescout.suggestions.presentation.usecase.BuildUpdateSuggestionsErrorNotification
import cinescout.suggestions.presentation.usecase.BuildUpdateSuggestionsForegroundNotification
import cinescout.suggestions.presentation.usecase.BuildUpdateSuggestionsSuccessNotification
import cinescout.suggestions.presentation.usecase.CreateUpdateSuggestionsGroup
import cinescout.utils.android.setInput
import com.google.firebase.analytics.FirebaseAnalytics
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import org.koin.ksp.generated.module as generatedModule

class UpdateSuggestionsWorkerTest : AutoCloseKoinTest() {

    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var workManager: WorkManager
    private val updateSuggestions = FakeUpdateSuggestions()
    private val testModule = module {
        single<CoroutineDispatcher> { dispatcher }
        single<CoroutineDispatcher>(named("io dispatcher")) { dispatcher }
        single {
            BuildUpdateSuggestionsErrorNotification(
                context = get(),
                notificationManagerCompat = get(),
                createUpdateSuggestionsGroup = get()
            )
        }
        single {
            BuildUpdateSuggestionsForegroundNotification(
                context = get(),
                notificationManagerCompat = get(),
                createUpdateSuggestionsGroup = get()
            )
        }
        single {
            BuildUpdateSuggestionsSuccessNotification(
                context = get(),
                notificationManagerCompat = get(),
                createUpdateSuggestionsGroup = get()
            )
        }
        single {
            CreateUpdateSuggestionsGroup(
                context = get(),
                notificationManagerCompat = NotificationManagerCompat.from(get())
            )
        }
        single<FirebaseAnalytics> { mockk(relaxed = true) }
        single { NotificationManagerCompat.from(get()) }
        single<UpdateSuggestions> { updateSuggestions }
        worker { (parameters: WorkerParameters) ->
            spyk(
                UpdateSuggestionsWorker(
                    appContext = get(),
                    params = parameters,
                    analytics = get(),
                    buildUpdateSuggestionsErrorNotification = get(),
                    buildUpdateSuggestionsForegroundNotification = get(),
                    buildUpdateSuggestionsSuccessNotification = get(),
                    ioDispatcher = dispatcher,
                    notificationManagerCompat = get(),
                    updateSuggestions = get()
                )
            ) {
                @Suppress("DEPRECATION")
                every { coroutineContext } returns dispatcher
                every { runAttemptCount } returns Int.MAX_VALUE
            }
        }
    }

    @BeforeTest
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        // Koin
        startKoin {
            androidContext(context)
            modules(
                SuggestionsPresentationModule().generatedModule,
                testModule
            )
        }.koin

        // Work manager
        val workerFactory = DelegatingWorkerFactory()
            .apply {
                addFactory(object : WorkerFactory() {
                    override fun createWorker(
                        appContext: Context,
                        workerClassName: String,
                        workerParameters: WorkerParameters
                    ) = get<UpdateSuggestionsWorker> { parametersOf(workerParameters) }
                })
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
        val workInfo = workManager.getWorkInfoById(request.id).get()

        // then
        assertEquals(expected, workInfo.state, message = workInfo.toString())
    }

    @Test
    fun failsWhenUpdateSuggestionsFails() {
        // given
        val expected = WorkInfo.State.FAILED
        updateSuggestions.error = NetworkError.NoNetwork

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
