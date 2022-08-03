package cinescout.android

import cinescout.android.testutil.homeRobot
import cinescout.android.testutil.runComposeAppTest
import cinescout.test.compose.robot.HomeRobot.Companion.verify
import kotlin.test.Test

class DrawerFlowTest {

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
}
