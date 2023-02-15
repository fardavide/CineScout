package cinescout.test.compose.robot

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import cinescout.design.TestTag
import cinescout.design.TextRes
import cinescout.test.compose.util.hasText

context(ComposeUiTest)
class TvShowDetailsRobot internal constructor() {

    fun awaitIdle(): TvShowDetailsRobot {
        waitForIdle()
        return this
    }

    fun verify(block: Verify.() -> Unit): TvShowDetailsRobot {
        block(Verify())
        return this
    }

    context(ComposeUiTest)
    class Verify internal constructor() {

        fun bannerIsDisplayed(message: TextRes) {
            onNode(hasParent(hasTestTag(TestTag.Banner)) and hasText(message))
                .assertIsDisplayed()
        }
        
        fun tvShowDetailsIsDisplayed() {
            onNodeWithTag(TestTag.TvShowDetails)
                .assertIsDisplayed()
        }

        fun titleIsDisplayed(title: String) {
            onNodeWithText(title)
                .assertIsDisplayed()
        }
    }
}

context(ComposeUiTest)
fun TvShowDetailsRobot(content: @Composable () -> Unit): TvShowDetailsRobot {
    setContent(content)
    return TvShowDetailsRobot()
}
