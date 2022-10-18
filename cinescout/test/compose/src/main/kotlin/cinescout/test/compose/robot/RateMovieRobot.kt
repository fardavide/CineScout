package cinescout.test.compose.robot

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.click
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import cinescout.design.TestTag

class RateMovieRobot<T : ComponentActivity> internal constructor(
    private val composeTest: AndroidComposeUiTest<T>
) {

    fun selectRating(rating: Int) {
        composeTest.onNodeWithTag(TestTag.RateItemSlider).performTouchInput {
            val offset = rating.toFloat() / 10
            click(percentOffset(offset, 0.5f))
        }
    }

    fun verify(block: Verify<T>.() -> Unit) = Verify(composeTest).apply(block)

    class Verify<T : ComponentActivity> internal constructor(
        private val composeTest: AndroidComposeUiTest<T>
    ) {

        fun hasRating(rating: Int) {
            composeTest.onNodeWithText(rating.toString())
                .assertIsDisplayed()
        }

        fun hasTitle(title: String) {
            composeTest.onNodeWithText(title, substring = true)
                .assertIsDisplayed()
        }
    }
}

fun <T : ComponentActivity> AndroidComposeUiTest<T>.RateMovieRobot(content: @Composable () -> Unit) =
    RateMovieRobot(this).also { setContent(content) }
