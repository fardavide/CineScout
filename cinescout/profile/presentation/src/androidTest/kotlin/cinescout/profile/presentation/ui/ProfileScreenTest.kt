package cinescout.profile.presentation.ui

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.runComposeUiTest
import cinescout.profile.presentation.sample.ProfileAccountUiModelSample
import cinescout.profile.presentation.sample.ProfileStateSample
import cinescout.profile.presentation.state.ProfileState
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.test.compose.robot.ProfileRobot
import kotlin.test.Test

class ProfileScreenTest {

    @Test
    fun appVersionIsDisplayed() = runComposeUiTest {
        setupScreen(state = ProfileStateSample.AccountNotConnected)
            .verify {
                appVersionIsDisplayed(version = ProfileStateSample.AccountNotConnected.appVersion)
            }
    }

    @Test
    fun manageAccountHintIsDisplayed() = runComposeUiTest {
        setupScreen(state = ProfileState.Loading)
            .verify { manageAccountHintIsDisplayed() }
    }

    @Test
    fun whenAccountConnected_thenUsernameIsDisplayed() = runComposeUiTest {
        setupScreen(state = ProfileStateSample.AccountConnected)
            .verify { usernameIsDisplayed(text = ProfileAccountUiModelSample.Account.username) }
    }

    @Test
    fun whenAccountError_thenErrorMessageIsDisplayed() = runComposeUiTest {
        setupScreen(state = ProfileStateSample.AccountError)
            .verify { errorMessageIsDisplayed(TextRes(string.profile_account_error)) }
    }

    context(ComposeUiTest)
    private fun setupScreen(state: ProfileState) =
        ProfileRobot { ProfileScreen(state = state, actions = ProfileScreen.Actions.Empty) }
}
