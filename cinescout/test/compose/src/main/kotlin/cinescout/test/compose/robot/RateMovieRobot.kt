package cinescout.test.compose.robot

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.click
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import cinescout.design.TestTag

context(ComposeUiTest)
class RateMovieRobot internal constructor() {

    fun selectRating(rating: Int) {
        onNodeWithTag(TestTag.RateItemSlider).performTouchInput {
            val offset = rating.toFloat() / 10
            click(percentOffset(offset, 0.5f))
        }
    }

    fun verify(block: Verify.() -> Unit): RateMovieRobot {
        block(Verify())
        return this
    }

    context(ComposeUiTest)
    class Verify internal constructor() {

        fun hasRating(rating: Int) {
            onNodeWithText(rating.toString())
                .assertIsDisplayed()
        }

        fun hasTitle(title: String) {
            onNodeWithText(title, substring = true)
                .assertIsDisplayed()
        }
    }
}

context(ComposeUiTest)
fun RateMovieRobot(content: @Composable () -> Unit): RateMovieRobot {
    setContent(content)
    return RateMovieRobot()
}
