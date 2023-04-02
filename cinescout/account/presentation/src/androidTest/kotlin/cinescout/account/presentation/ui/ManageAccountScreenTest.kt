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
    fun whenNotConnected_connectButtonIsDisplayed() = runComposeTest {
        setupScreen(state = ManageAccountStateSample.NotConnected)
            .verify { connectButtonIsDisplayed() }
    }

    @Test
    fun whenError_messageIsDisplayed() = runComposeTest {
        setupScreen(state = ManageAccountStateSample.Error)
            .verify { errorIsDisplayed(ManageAccountStateSample.Account.Error.message) }
    }

    @Test
    fun whenConnectToTraktIsSelected_actionIsInvoked() = runComposeTest {
        var invoked = false
        val linkActions = ManageAccountScreen.LinkActions.Empty.copy(
            link = { invoked = true }
        )
        setupScreen(state = ManageAccountStateSample.NotConnected, linkActions = linkActions)
            .selectConnectToTrakt()
        assertTrue(invoked)
    }

    @Test
    fun whenConnectedToTrakt_accountUsernameIsDisplayed() = runComposeTest {
        setupScreen(state = ManageAccountStateSample.Connected)
            .verify { accountUsernameIsDisplayed(ManageAccountStateSample.Account.Connected.uiModel.username) }
    }

    @Test
    fun whenConnectedToTrakt_disconnectButtonIsDisplayed() = runComposeTest {
        setupScreen(state = ManageAccountStateSample.Connected)
            .verify { disconnectButtonIsDisplayed() }
    }

    private fun ComposeUiTest.setupScreen(
        state: ManageAccountState,
        linkActions: ManageAccountScreen.LinkActions = ManageAccountScreen.LinkActions.Empty
    ) = ManageAccountRobot {
        ManageAccountScreen(state = state, linkActions = linkActions, back = {})
    }
}
