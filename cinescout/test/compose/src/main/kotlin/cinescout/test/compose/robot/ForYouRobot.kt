package cinescout.test.compose.robot

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import cinescout.test.compose.util.awaitDisplayed
import cinescout.test.compose.util.onNodeWithText
import studio.forface.cinescout.design.R.string

class ForYouRobot<T : ComponentActivity> internal constructor(
    composeTest: AndroidComposeUiTest<T>
) : HomeRobot<T>(composeTest) {

    fun awaitIdle(): ForYouRobot<T> {
        composeTest.waitForIdle()
        return this
    }

    fun dismissHint(): ForYouRobot<T> {
        composeTest.onNodeWithText(string.suggestions_for_you_hint_dismiss)
            .performClick()

        return this
    }

    fun openMovieDetails(): MovieDetailsRobot<T> {
        composeTest.onNodeWithText(string.suggestions_for_you_open_details)
            .awaitDisplayed(composeTest)
            .performClick()

        return MovieDetailsRobot(composeTest)
    }
    
    fun openTvShowDetails(): TvShowDetailsRobot<T> {
        composeTest.onNodeWithText(string.suggestions_for_you_open_details)
            .awaitDisplayed(composeTest)
            .performClick()

        return TvShowDetailsRobot(composeTest)
    }

    fun performLikeAction(fraction: Float = 1f, performUp: Boolean = true): ForYouRobot<T> {
        composeTest.onRoot()
            .performTouchInput {
                down(Offset(centerX, centerY))
                val endX = centerX + centerX * fraction
                moveTo(Offset(endX, centerY))
                if (performUp) {
                    up()
                }
            }

        return this
    }

    fun selectMoviesType(): ForYouRobot<T> {
        composeTest.onNodeWithText(string.item_type_movies)
            .performClick()
        return this
    }

    fun selectTvShowsType(): ForYouRobot<T> {
        composeTest.onNodeWithText(string.item_type_tv_shows)
            .performClick()
        return this
    }

    class Verify<T : ComponentActivity>(composeTest: AndroidComposeUiTest<T>) : HomeRobot.Verify<T>(composeTest) {

        fun movieIsDisplayed(movieTitle: String) {
            composeTest.onNodeWithText(movieTitle)
                .assertIsDisplayed()
        }

        fun moviesTypeIsSelected() {
            composeTest.onNodeWithText(string.item_type_movies)
                .assertIsSelected()
        }

        fun tvShowIsDisplayed(tvShowTitle: String) {
            composeTest.onNodeWithText(tvShowTitle)
                .assertIsDisplayed()
        }

        fun tvShowsTypeIsSelected() {
            composeTest.onNodeWithText(string.item_type_tv_shows)
                .assertIsSelected()
        }
    }

    companion object {

        fun <T : ComponentActivity> ForYouRobot<T>.verify(block: Verify<T>.() -> Unit): ForYouRobot<T> =
            also { Verify(composeTest).block() }
    }
}

fun <T : ComponentActivity> AndroidComposeUiTest<T>.ForYouRobot(content: @Composable () -> Unit) =
    ForYouRobot(this).also { setContent(content) }
