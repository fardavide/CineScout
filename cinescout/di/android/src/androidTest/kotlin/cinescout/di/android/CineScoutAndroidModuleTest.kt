package cinescout.di.android

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.SavedStateHandle
import androidx.test.core.app.ApplicationProvider
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import com.google.firebase.analytics.FirebaseAnalytics
import io.mockk.mockkClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.TestScope
import org.junit.Rule
import org.koin.core.error.InstanceCreationException
import org.koin.ksp.generated.module
import org.koin.test.check.checkKoinModules
import org.koin.test.mock.MockProviderRule
import kotlin.test.Ignore
import kotlin.test.Test

class CineScoutAndroidModuleTest {

    @get:Rule
    val mockProvider = MockProviderRule.create { kClass ->
        mockkClass(kClass, relaxed = true)
    }

    @Test
    @Ignore(
        "MockK issue?" +
            "java.lang.AbstractMethodError: 'public abstract java.io.File android.content.Context.getCacheDir()' " +
            "cannot be called from a static context"
    )
    fun verifyAndroidModules() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        try {
            checkKoinModules(listOf(CineScoutAndroidModule().module)) {
                // platform
                withInstance<Context>(context)
                withInstance<CoroutineScope>(TestScope())
                withInstance<FirebaseAnalytics>()
                withInstance<NotificationManagerCompat>()
                withInstance<SavedStateHandle>()
                withInstance<WorkerParameters>()
                withInstance<WorkManager>()

                // app
                withInstance(123) // app version
                withInstance(ScreenplayType.All)
                withInstance(TmdbScreenplayIdSample.Inception)
                withInstance(TmdbScreenplayIdSample.Dexter)
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
