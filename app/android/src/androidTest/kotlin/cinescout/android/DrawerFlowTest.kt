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

class DrawerFlowTest {

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
    fun givenDrawerIsOpen_whenAccountsIsClicked_loginIsShown() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openAccounts()
            .verify {
                connectToTmdbIsDisplayed()
                connectToTraktIsDisplayed()
            }
    }

    @Test
    fun givenDrawerIsOpen_whenAccountsIsClicked_drawerIsClosed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openAccounts()
            .close()
            .verify { drawerIsClosed() }
    }

    @Test
    fun givenDrawerIsOpen_whenForYouIsClicked_drawerIsClosed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openForYou()
            .verify { drawerIsClosed() }
    }

    @Test
    fun givenDrawerIsOpen_whenForYouIsClicked_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openForYou()
            .verify { forYouIsDisplayed() }
    }

    @Test
    fun givenDrawerIsOpen_whenWatchlistIsClicked_drawerIsClosed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openWatchlist()
            .verify { drawerIsClosed() }
    }

    @Test
    fun givenDrawerIsOpen_whenWatchlistIsClicked_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openWatchlist()
            .verify { watchlistIsDisplayed() }
    }

    @Test
    fun givenDrawerIsOpen_whenMyListsIsClicked_drawerIsClosed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openMyLists()
            .verify { drawerIsClosed() }
    }

    @Test
    fun givenDrawerIsOpen_whenMyListsIsClicked_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openMyLists()
            .verify { myListsIsDisplayed() }
    }
}
