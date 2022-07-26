package cinescout.android

import cinescout.android.testutil.homeRobot
import cinescout.android.testutil.runComposeAppTest
import kotlin.test.Test

class DrawerTest {

    @Test
    fun openDrawerAndOpenLogin() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openLogin()
            .verify {
                loginToTmdbIsDisplayed()
                loginToTraktIsDisplayed()
            }
    }
}
