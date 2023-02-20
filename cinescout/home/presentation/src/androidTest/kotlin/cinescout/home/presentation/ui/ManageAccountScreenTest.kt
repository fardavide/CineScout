package cinescout.home.presentation.ui

import androidx.compose.ui.test.ComposeUiTest
import cinescout.design.testdata.MessageSample
import cinescout.home.presentation.model.ManageAccountState
import cinescout.home.presentation.sample.ManageAccountStateSample
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
        setupScreen(state = ManageAccountState.NotConnected)
            .verify { connectButtonsAreDisplayed() }
    }

    @Test
    fun whenError_messageIsDisplayed() = runComposeTest {
        val message = MessageSample.NoNetworkError
        setupScreen(state = ManageAccountState.Error(message))
            .verify { errorIsDisplayed(message) }
    }
    
    @Test
    fun whenConnectToTmdbIsSelected_actionIsInvoked() = runComposeTest {
        var invoked = false
        val linkActions = ManageAccountScreen.LinkActions.Empty.copy(
            linkToTmdb = { invoked = true }
        )
        setupScreen(state = ManageAccountState.NotConnected, linkActions = linkActions)
            .selectConnectToTmdb()
        assertTrue(invoked)
    }

    @Test
    fun whenConnectToTraktIsSelected_actionIsInvoked() = runComposeTest {
        var invoked = false
        val linkActions = ManageAccountScreen.LinkActions.Empty.copy(
            linkToTrakt = { invoked = true }
        )
        setupScreen(state = ManageAccountState.NotConnected, linkActions = linkActions)
            .selectConnectToTrakt()
        assertTrue(invoked)
    }

    @Test
    fun whenConnectedToTmdb_accountUsernameIsDisplayed() = runComposeTest {
        val state = ManageAccountStateSample.TmdbConnected
        setupScreen(state = state)
            .verify { accountUsernameIsDisplayed(state.uiModel.username) }
    }

    @Test
    fun whenConnectedToTrakt_accountUsernameIsDisplayed() = runComposeTest {
        val state = ManageAccountStateSample.TraktConnected
        setupScreen(state = state)
            .verify { accountUsernameIsDisplayed(state.uiModel.username) }
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
