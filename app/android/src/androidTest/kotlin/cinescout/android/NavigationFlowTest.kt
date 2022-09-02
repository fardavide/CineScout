package cinescout.android

import android.Manifest
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import cinescout.android.testutil.homeRobot
import cinescout.android.testutil.runComposeAppTest
import cinescout.test.compose.robot.HomeRobot.Companion.verify
import kotlin.test.BeforeTest
import kotlin.test.Test

class NavigationFlowTest {

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
    fun givenHomeIsDisplayed_whenForYouIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .selectForYou()
            .verify { forYouIsDisplayed() }
    }

    @Test
    fun givenHomeIsDisplayed_whenForYouIsOpen_subtitleIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .selectForYou()
            .verify { forYouSubtitleIsDisplayed() }
    }

    @Test
    fun givenHomeIsDisplayed_whenMyListsIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .selectMyLists()
            .verify { myListsIsDisplayed() }
    }

    @Test
    fun givenHomeIsDisplayed_whenMyListsIsOpen_subtitleIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .selectMyLists()
            .verify { myListSubtitleIsDisplayed() }
    }

    @Test
    fun givenHomeIsDisplayed_whenWatchlistIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .selectWatchlist()
            .verify { watchlistIsDisplayed() }
    }

    @Test
    fun givenHomeIsDisplayed_whenWatchlistIsOpen_subtitleIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .selectWatchlist()
            .verify { watchlistSubtitleIsDisplayed() }
    }

    @Test
    fun givenMyListsIsDisplayed_whenRatedIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openMyLists()
            .selectRated()
            .verify { ratedIsDisplayed() }
    }

    @Test
    fun givenMyListsIsDisplayed_whenRatedIsOpen_subtitleIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openMyLists()
            .selectRated()
            .verify { ratedSubtitleIsDisplayed() }
    }
}
