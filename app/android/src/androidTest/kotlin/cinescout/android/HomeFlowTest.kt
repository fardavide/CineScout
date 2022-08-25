package cinescout.android

import android.Manifest
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import cinescout.android.testutil.homeRobot
import cinescout.android.testutil.runComposeAppTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class HomeFlowTest {

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
    fun givenHomeIsDisplayed_whenForYouIsOpen_subtitleIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .selectForYou()
            .verify { forYouSubtitleIsDisplayed() }
    }

    @Test
    fun givenHomeIsDisplayed_whenWatchlistIsOpen_subtitleIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .selectWatchlist()
            .verify { watchlistSubtitleIsDisplayed() }
    }
}
