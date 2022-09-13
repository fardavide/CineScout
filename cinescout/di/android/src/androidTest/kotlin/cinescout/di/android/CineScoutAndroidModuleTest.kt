package cinescout.di.android

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.SavedStateHandle
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import cinescout.di.kotlin.AppVersionQualifier
import com.google.firebase.analytics.FirebaseAnalytics
import io.mockk.mockkClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.TestScope
import org.junit.Rule
import org.koin.core.error.InstanceCreationException
import org.koin.dsl.module
import org.koin.test.check.checkKoinModules
import org.koin.test.mock.MockProviderRule
import kotlin.test.Test

class CineScoutAndroidModuleTest {

    @get:Rule
    val mockProvider = MockProviderRule.create { kClass ->
        mockkClass(kClass, relaxed = true)
    }

    private val extraModule = module {
        factory(AppVersionQualifier) { 123 }
        factory<CoroutineScope> { TestScope() }
    }

    @Test
    fun verifyAndroidModules() {
        try {
            checkKoinModules(listOf(CineScoutAndroidModule, extraModule)) {
                withInstance<Context>()
                withInstance<FirebaseAnalytics>()
                withInstance<NotificationManagerCompat>()
                withInstance<SavedStateHandle>()
                withInstance<WorkerParameters>()
                withInstance<WorkManager>()
            }
        } catch (e: InstanceCreationException) {
            throw e.getRootCause()
        }
    }

    private fun Throwable.getRootCause(): Throwable {
        var rootCause: Throwable? = this
        while (rootCause?.cause != null) {
            rootCause = rootCause.cause
        }
        return checkNotNull(rootCause)
    }
}
