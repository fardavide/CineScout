package cinescout.test.compose.robot

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performClick
import cinescout.test.compose.semantic.HomeSemantics
import cinescout.test.compose.semantic.ManageAccountSemantics
import cinescout.test.compose.semantic.ProfileSemantics
import cinescout.test.compose.semantic.ReportBugSemantics
import cinescout.test.compose.semantic.RequestFeatureSemantics
import cinescout.test.compose.semantic.SettingsSemantics

context(ComposeUiTest, ProfileSemantics)
class ProfileRobot internal constructor() {

    fun openManageAccount(): ManageAccountRobot {
        manageAccount().performClick()
        return ManageAccountSemantics { ManageAccountRobot() }
    }

    fun openRequestFeature(): RequestFeatureRobot {
        requestFeature().performClick()
        return RequestFeatureSemantics { RequestFeatureRobot() }
    }

    fun openReportBug(): ReportBugRobot {
        reportBug().performClick()
        return ReportBugSemantics { ReportBugRobot() }
    }

    fun openSettings(): SettingsRobot {
        settings().performClick()
        return SettingsSemantics { SettingsRobot() }
    }

    fun verify(block: Verify.() -> Unit): ProfileRobot {
        HomeSemantics { block(Verify()) }
        return this
    }

    context(ComposeUiTest, ProfileSemantics, HomeSemantics)
    class Verify internal constructor() : HomeRobot.Verify() {

        fun appVersionIsDisplayed(version: Int) {
            appVersion(version = version).assertIsDisplayed()
        }

        fun manageAccountHintIsDisplayed() {
            manageAccountHint().assertIsDisplayed()
        }

        fun screenIsDisplayed() {
            screen().assertIsDisplayed()
        }

        fun titleIsDisplayed() {
            title().assertIsDisplayed()
        }

        fun usernameIsDisplayed(text: String) {
            username(text = text).assertIsDisplayed()
        }
    }
}

context(ComposeUiTest)
fun ProfileRobot(content: @Composable () -> Unit): ProfileRobot {
    setContent(content)
    return ProfileSemantics { ProfileRobot() }
}
