package cinescout.test.compose.robot

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import cinescout.design.TestTag
import cinescout.design.TextRes
import cinescout.test.compose.util.hasText

class TvShowDetailsRobot<T : ComponentActivity> internal constructor(
    private val composeTest: AndroidComposeUiTest<T>
) {

    fun awaitIdle(): TvShowDetailsRobot<T> {
        composeTest.waitForIdle()
        return this
    }

    fun verify(block: Verify<T>.() -> Unit) = Verify(composeTest).apply(block)

    class Verify<T : ComponentActivity> internal constructor(
        private val composeTest: AndroidComposeUiTest<T>
    ) {

        fun bannerIsDisplayed(message: TextRes) {
            composeTest.onNode(hasParent(hasTestTag(TestTag.Banner)) and hasText(message))
                .assertIsDisplayed()
        }
        
        fun tvShowDetailsIsDisplayed() {
            composeTest.onNodeWithTag(TestTag.TvShowDetails)
                .assertIsDisplayed()
        }

        fun titleIsDisplayed(title: String) {
            composeTest.onNodeWithText(title)
                .assertIsDisplayed()
        }
    }
}

fun <T : ComponentActivity> AndroidComposeUiTest<T>.TvShowDetailsRobot(content: @Composable () -> Unit) =
    TvShowDetailsRobot(this).also { setContent(content) }
