package cinescout.test.compose.robot

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import cinescout.design.TestTag
import cinescout.resources.TextRes
import cinescout.test.compose.util.hasText

context(ComposeUiTest)
class ScreenplayDetailsRobot internal constructor() {

    fun awaitIdle(): ScreenplayDetailsRobot {
        waitForIdle()
        return this
    }

    fun verify(block: Verify.() -> Unit): ScreenplayDetailsRobot {
        block(Verify())
        return this
    }

    context(ComposeUiTest)
    class Verify internal constructor() {

        fun bannerIsDisplayed(message: TextRes) {
            onNode(hasParent(hasTestTag(TestTag.Banner)) and hasText(message))
                .assertIsDisplayed()
        }

        fun detailsIsDisplayed() {
            onNodeWithTag(TestTag.ScreenplayDetails)
                .assertIsDisplayed()
        }

        fun titleIsDisplayed(title: String) {
            onNodeWithText(title)
                .assertIsDisplayed()
        }
    }
}

context(ComposeUiTest)
fun MovieDetailsRobot(content: @Composable () -> Unit): ScreenplayDetailsRobot {
    setContent(content)
    return ScreenplayDetailsRobot()
}
