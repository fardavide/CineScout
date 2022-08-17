package cinescout.android

import android.Manifest
import android.os.Build
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import cinescout.android.testutil.runComposeAppTest
import cinescout.design.TestTag
import kotlin.test.BeforeTest
import kotlin.test.Test

class AndroidAppTest {

    @BeforeTest
    fun setup() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val context = ApplicationProvider.getApplicationContext<CineScoutApplication>()
            InstrumentationRegistry.getInstrumentation()
                .uiAutomation
                .grantRuntimePermission(context.packageName, Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    @Test
    fun homeScreenIsShownAtStart() = runComposeAppTest {
        onNodeWithTag(TestTag.Home)
            .assertIsDisplayed()
    }
}
