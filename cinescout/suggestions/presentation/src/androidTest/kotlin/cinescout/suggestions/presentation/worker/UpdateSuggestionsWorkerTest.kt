package cinescout.suggestions.presentation.worker

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.test.core.app.ApplicationProvider
import androidx.work.DelegatingWorkerFactory
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.testing.TestListenableWorkerBuilder
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
import cinescout.test.android.setInput
import com.google.firebase.analytics.FirebaseAnalytics
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import org.koin.ksp.generated.module as generatedModule

class UpdateSuggestionsWorkerTest : AutoCloseKoinTest() {

    private val dispatcher = UnconfinedTestDispatcher()
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

    private lateinit var workerBuilder: TestListenableWorkerBuilder<UpdateSuggestionsWorker>

    private val workerId = UUID.randomUUID()

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

        workerBuilder =
            TestListenableWorkerBuilder<UpdateSuggestionsWorker>(ApplicationProvider.getApplicationContext())
                .setWorkerFactory(workerFactory)
                .setId(workerId)
    }

    @Test
    fun failsWhenNoInput() = runTest(dispatcher) {
        // given
        val worker = workerBuilder
            .build()

        // when - then
        assertFailsWith<IllegalArgumentException> {
            worker.doWork()
        }
    }

    @Test
    fun failsWhenWrongInput() = runTest(dispatcher) {
        // given
        val worker = workerBuilder
            .setInputData(workDataOf("key" to "value"))
            .build()

        // when - then
        assertFailsWith<IllegalArgumentException> {
            worker.doWork()
        }
    }

    @Test
    fun succeedWhenRightInput() = runTest(dispatcher) {
        // given
        val worker = workerBuilder
            .setInput(SuggestionsMode.Deep)
            .build()

        // when
        val result = worker.doWork()

        // then
        assert(result is ListenableWorker.Result.Success)
    }

    @Test
    fun failsWhenUpdateSuggestionsFails() = runTest(dispatcher) {
        // given
        updateSuggestions.error = NetworkError.NoNetwork

        // when
        val worker = workerBuilder
            .setInput(SuggestionsMode.Deep)
            .build()

        // then
        val result = worker.doWork()
        assert(result is ListenableWorker.Result.Failure)
    }
}
