package cinescout.account.presentation.ui

import androidx.compose.ui.test.ComposeUiTest
import cinescout.account.presentation.sample.ManageAccountStateSample
import cinescout.account.presentation.state.ManageAccountState
import cinescout.test.compose.robot.ManageAccountRobot
import cinescout.test.compose.runComposeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class ManageAccountScreenTest {

    @Test
    fun whenLoading_progressIsDisplayed() = runComposeTest {
        setupScreen(state = ManageAccountState.Loading)
            .verify { progressIsDisplayed() }
    }

    @Test
    fun whenNotConnected_connectButtonsAreDisplayed() = runComposeTest {
        setupScreen(state = ManageAccountStateSample.NotConnected)
            .verify { connectButtonsAreDisplayed() }
    }

    @Test
    fun whenError_messageIsDisplayed() = runComposeTest {
        setupScreen(state = ManageAccountStateSample.Error)
            .verify { errorIsDisplayed(ManageAccountStateSample.Account.Error.message) }
    }
    
    @Test
    fun whenConnectToTmdbIsSelected_actionIsInvoked() = runComposeTest {
        var invoked = false
        val linkActions = ManageAccountScreen.LinkActions.Empty.copy(
            linkToTmdb = { invoked = true }
        )
        setupScreen(state = ManageAccountStateSample.NotConnected, linkActions = linkActions)
            .selectConnectToTmdb()
        assertTrue(invoked)
    }

    @Test
    fun whenConnectToTraktIsSelected_actionIsInvoked() = runComposeTest {
        var invoked = false
        val linkActions = ManageAccountScreen.LinkActions.Empty.copy(
            linkToTrakt = { invoked = true }
        )
        setupScreen(state = ManageAccountStateSample.NotConnected, linkActions = linkActions)
            .selectConnectToTrakt()
        assertTrue(invoked)
    }

    @Test
    fun whenConnectedToTmdb_accountUsernameIsDisplayed() = runComposeTest {
        setupScreen(state = ManageAccountStateSample.TmdbConnected)
            .verify { accountUsernameIsDisplayed(ManageAccountStateSample.Account.TmdbConnected.uiModel.username) }
    }

    @Test
    fun whenConnectedToTrakt_accountUsernameIsDisplayed() = runComposeTest {
        setupScreen(state = ManageAccountStateSample.TraktConnected)
            .verify { accountUsernameIsDisplayed(ManageAccountStateSample.Account.TraktConnected.uiModel.username) }
    }

    @Test
    fun whenConnectedToTmdb_disconnectButtonIsDisplayed() = runComposeTest {
        setupScreen(state = ManageAccountStateSample.TmdbConnected)
            .verify { disconnectButtonIsDisplayed() }
    }

    @Test
    fun whenConnectedToTrakt_disconnectButtonIsDisplayed() = runComposeTest {
        setupScreen(state = ManageAccountStateSample.TraktConnected)
            .verify { disconnectButtonIsDisplayed() }
    }

    private fun ComposeUiTest.setupScreen(
        state: ManageAccountState,
        linkActions: ManageAccountScreen.LinkActions = ManageAccountScreen.LinkActions.Empty
    ) = ManageAccountRobot {
        ManageAccountScreen(state = state, linkActions = linkActions, back = {})
    }
}
