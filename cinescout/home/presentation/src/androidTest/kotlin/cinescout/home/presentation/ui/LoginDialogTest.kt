package cinescout.home.presentation.ui

import androidx.compose.runtime.Composable
import cinescout.test.compose.robot.LoginRobot
import cinescout.test.compose.runComposeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LoginDialogTest {

    @Test
    fun performLoginToTmdbWhenButtonIsClicked() = runComposeTest {
        var wasLoginToTmdbCalled = false
        var wasLoginToTraktCalled = false
        val loginActions = LoginActions(
            loginToTmdb = { wasLoginToTmdbCalled = true },
            loginToTrakt = { wasLoginToTraktCalled = true }
        )
        LoginRobot { LoginDialog(loginActions) }
            .selectTmdb()
            .verify {
                assertTrue(wasLoginToTmdbCalled)
                assertFalse(wasLoginToTraktCalled)
            }
    }

    @Test
    fun performLoginToTraktWhenButtonIsClicked() = runComposeTest {
        var wasLoginToTmdbCalled = false
        var wasLoginToTraktCalled = false
        val loginActions = LoginActions(
            loginToTmdb = { wasLoginToTmdbCalled = true },
            loginToTrakt = { wasLoginToTraktCalled = true }
        )
        LoginRobot { LoginDialog(loginActions) }
            .selectTrakt()
            .verify {
                assertFalse(wasLoginToTmdbCalled)
                assertTrue(wasLoginToTraktCalled)
            }
    }

    @Composable
    private fun LoginDialog(loginActions: LoginActions) {
        val actions = LoginDialog.Actions(loginActions = loginActions, onDismissRequest = {})
        LoginDialog(actions = actions)
    }
}
