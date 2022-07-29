package cinescout.android

import cinescout.android.testutil.homeRobot
import cinescout.android.testutil.runComposeAppTest
import kotlin.test.Test

class DrawerFlowTest {

    @Test
    fun givenDrawerIsOpen_whenLoginIsClicked_loginIsShown() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openLogin()
            .verify {
                loginToTmdbIsDisplayed()
                loginToTraktIsDisplayed()
            }
    }

    @Test
    fun givenDrawerIsOpen_whenLoginIsClicked_drawerIsClosed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openLogin()
            .closeLogin()
            .verify { drawerIsClosed() }
    }
}
