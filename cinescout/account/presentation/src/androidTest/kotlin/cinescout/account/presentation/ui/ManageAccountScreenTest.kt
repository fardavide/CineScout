package cinescout.account.presentation.ui

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.runComposeUiTest
import cinescout.account.presentation.sample.ManageAccountStateSample
import cinescout.account.presentation.state.ManageAccountState
import cinescout.test.compose.robot.ManageAccountRobot
import kotlin.test.Test
import kotlin.test.assertTrue

class ManageAccountScreenTest {

    @Test
    fun whenLoading_progressIsDisplayed() = runComposeUiTest {
        setupScreen(state = ManageAccountState.Loading)
            .verify { progressIsDisplayed() }
    }

    @Test
    fun whenNotConnected_connectButtonIsDisplayed() = runComposeUiTest {
        setupScreen(state = ManageAccountStateSample.NotConnected)
            .verify { connectButtonIsDisplayed() }
    }

    @Test
    fun whenError_messageIsDisplayed() = runComposeUiTest {
        setupScreen(state = ManageAccountStateSample.Error)
            .verify { errorIsDisplayed(ManageAccountStateSample.Account.Error.message) }
    }

    @Test
    fun whenConnectToTraktIsSelected_actionIsInvoked() = runComposeUiTest {
        var invoked = false
        val linkActions = ManageAccountScreen.LinkActions.Empty.copy(
            link = { invoked = true }
        )
        setupScreen(state = ManageAccountStateSample.NotConnected, linkActions = linkActions)
            .selectConnectToTrakt()
        assertTrue(invoked)
    }

    @Test
    fun whenConnectedToTrakt_accountUsernameIsDisplayed() = runComposeUiTest {
        setupScreen(state = ManageAccountStateSample.Connected)
            .verify {
                accountUsernameIsDisplayed(ManageAccountStateSample.Account.Connected.uiModel.username)
            }
    }

    @Test
    fun whenConnectedToTrakt_disconnectButtonIsDisplayed() = runComposeUiTest {
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
