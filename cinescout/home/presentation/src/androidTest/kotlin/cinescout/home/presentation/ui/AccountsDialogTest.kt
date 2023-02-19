package cinescout.home.presentation.ui

import androidx.compose.runtime.Composable
import cinescout.home.presentation.model.HomeState
import cinescout.home.presentation.sample.HomeStateSample
import cinescout.home.presentation.sample.HomeStateSample.buildHomeState
import cinescout.test.compose.robot.AccountsRobot
import cinescout.test.compose.runComposeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AccountsDialogTest {

    @Test
    fun givenTmdbNotConnected_whenButtonIsClicked_performLogin() = runComposeTest {
        var wasLoginToTmdbCalled = false
        var wasLoginToTraktCalled = false
        val loginActions = LoginActions(
            loginToTmdb = { wasLoginToTmdbCalled = true },
            loginToTrakt = { wasLoginToTraktCalled = true }
        )
        AccountsRobot { AccountsDialog(loginActions = loginActions) }
            .selectTmdb()
            .verify {
                assertTrue(wasLoginToTmdbCalled)
                assertFalse(wasLoginToTraktCalled)
            }
    }

    @Test
    fun givenTmdbIsConnected_usernameIsDisplayed() = runComposeTest {
        val account = HomeStateSample.TmdbAccount
        val state = buildHomeState {
            accounts {
                tmdb = account
            }
        }.accounts
        AccountsRobot { AccountsDialog(state = state) }
            .verify {
                tmdbUsernameIsDisplayed(account.username)
            }
    }

    @Test
    fun givenTraktNotConnected_whenButtonIsClicked_performLogin() = runComposeTest {
        var wasLoginToTmdbCalled = false
        var wasLoginToTraktCalled = false
        val loginActions = LoginActions(
            loginToTmdb = { wasLoginToTmdbCalled = true },
            loginToTrakt = { wasLoginToTraktCalled = true }
        )
        AccountsRobot { AccountsDialog(loginActions = loginActions) }
            .selectTrakt()
            .verify {
                assertFalse(wasLoginToTmdbCalled)
                assertTrue(wasLoginToTraktCalled)
            }
    }

    @Test
    fun givenTraktIsConnected_usernameIsDisplayed() = runComposeTest {
        val account = HomeStateSample.TmdbAccount
        val state = buildHomeState {
            accounts {
                trakt = account
            }
        }.accounts
        AccountsRobot { AccountsDialog(state = state) }
            .verify {
                traktUsernameIsDisplayed(account.username)
            }
    }

    @Composable
    private fun AccountsDialog(
        state: HomeState.Accounts = buildHomeState().accounts,
        loginActions: LoginActions = LoginActions.Empty
    ) {
        val actions = AccountsDialog.Actions(loginActions = loginActions, onDismissRequest = {})
        AccountsDialog(state = state, actions = actions)
    }
}
