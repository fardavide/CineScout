package cinescout.profile.presentation.ui

import androidx.compose.ui.test.ComposeUiTest
import cinescout.profile.presentation.sample.ProfileAccountUiModelSample
import cinescout.profile.presentation.sample.ProfileStateSample
import cinescout.profile.presentation.state.ProfileState
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.test.compose.robot.ProfileRobot
import cinescout.test.compose.runComposeTest
import kotlin.test.Test

class ProfileScreenTest {

    @Test
    fun appVersionIsDisplayed() = runComposeTest {
        setupScreen(state = ProfileStateSample.AccountNotConnected)
            .verify { appVersionIsDisplayed(version = ProfileStateSample.AccountNotConnected.appVersion) }
    }

    @Test
    fun manageAccountHintIsDisplayed() = runComposeTest {
        setupScreen(state = ProfileState.Loading)
            .verify { manageAccountHintIsDisplayed() }
    }

    @Test
    fun whenAccountConnected_thenUsernameIsDisplayed() = runComposeTest {
        setupScreen(state = ProfileStateSample.AccountConnected)
            .verify { usernameIsDisplayed(text = ProfileAccountUiModelSample.Account.username) }
    }

    @Test
    fun whenAccountError_thenErrorMessageIsDisplayed() = runComposeTest {
        setupScreen(state = ProfileStateSample.AccountError)
            .verify { errorMessageIsDisplayed(TextRes(string.profile_account_error)) }
    }

    context(ComposeUiTest)
    private fun setupScreen(state: ProfileState) =
        ProfileRobot { ProfileScreen(state = state, actions = ProfileScreen.Actions.Empty) }
}
