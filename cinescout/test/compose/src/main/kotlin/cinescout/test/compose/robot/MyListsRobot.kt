package cinescout.test.compose.robot

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performClick
import cinescout.test.compose.util.onNodeWithText
import studio.forface.cinescout.design.R.string

class MyListsRobot<T : ComponentActivity> internal constructor(
    composeTest: AndroidComposeUiTest<T>
) : HomeRobot<T>(composeTest) {

    fun openDisliked(): ListRobot<T> {
        composeTest.onDisliked()
            .performClick()
        return ListRobot(composeTest)
    }

    fun openLiked(): ListRobot<T> {
        composeTest.onLiked()
            .performClick()
        return ListRobot(composeTest)
    }

    fun openRated(): ListRobot<T> {
        composeTest.onRated()
            .performClick()
        return ListRobot(composeTest)
    }

    fun selectDisliked(): MyListsRobot<T> {
        composeTest.onDisliked()
            .performClick()
        return this
    }

    fun selectLiked(): MyListsRobot<T> {
        composeTest.onLiked()
            .performClick()
        return this
    }

    fun selectRated(): MyListsRobot<T> {
        composeTest.onRated()
            .performClick()
        return this
    }

    class Verify<T : ComponentActivity>(composeTest: AndroidComposeUiTest<T>) : HomeRobot.Verify<T>(composeTest) {

        fun dislikedButtonIsDisplayed() {
            composeTest.onDisliked()
                .assertIsDisplayed()
        }

        fun likedButtonIsDisplayed() {
            composeTest.onLiked()
                .assertIsDisplayed()
        }

        fun ratedButtonIsDisplayed() {
            composeTest.onRated()
                .assertIsDisplayed()
        }
    }

    companion object {

        fun <T : ComponentActivity> MyListsRobot<T>.verify(
            block: MyListsRobot.Verify<T>.() -> Unit
        ): MyListsRobot<T> =
            also { MyListsRobot.Verify(composeTest).block() }
    }
}

fun <T : ComponentActivity> AndroidComposeUiTest<T>.MyListsRobot(content: @Composable () -> Unit) =
    MyListsRobot(this).also { setContent(content) }

fun <T : ComponentActivity> AndroidComposeUiTest<T>.onDisliked() =
    onNodeWithText(string.lists_disliked)

fun <T : ComponentActivity> AndroidComposeUiTest<T>.onLiked() =
    onNodeWithText(string.lists_liked)

fun <T : ComponentActivity> AndroidComposeUiTest<T>.onRated() =
    onNodeWithText(string.lists_rated)
