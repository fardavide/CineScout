package cinescout.test.compose.robot

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import cinescout.test.compose.util.awaitDisplayed
import cinescout.test.compose.util.onNodeWithText
import studio.forface.cinescout.design.R.string

class ListRobot<T : ComponentActivity> internal constructor(
    composeTest: AndroidComposeUiTest<T>
) : HomeRobot<T>(composeTest) {

    fun awaitIdle(): ListRobot<T> {
        composeTest.waitForIdle()
        return this
    }

    fun openMovie(title: String): MovieDetailsRobot<T> {
        composeTest.onNodeWithText(title)
            .awaitDisplayed(composeTest)
            .performClick()
        return MovieDetailsRobot(composeTest)
    }

    fun openTvShow(title: String): TvShowDetailsRobot<T> {
        composeTest.onNodeWithText(title)
            .performClick()
        return TvShowDetailsRobot(composeTest)
    }

    fun selectAllType(): ListRobot<T> {
        composeTest.onNodeWithText(string.item_type_all)
            .performClick()
        return this
    }

    fun selectMoviesType(): ListRobot<T> {
        composeTest.onNodeWithText(string.item_type_movies)
            .performClick()
        return this
    }

    fun selectTvShowsType(): ListRobot<T> {
        composeTest.onNodeWithText(string.item_type_tv_shows)
            .performClick()
        return this
    }

    class Verify<T : ComponentActivity>(composeTest: AndroidComposeUiTest<T>) : HomeRobot.Verify<T>(composeTest) {

        fun allTypeIsSelected() {
            composeTest.onNodeWithText(string.item_type_all)
                .assertIsSelected()
        }

        fun emptyAllWatchlistIsDisplayed() {
            composeTest.onNodeWithText(string.lists_watchlist_all_empty)
                .assertIsDisplayed()
        }

        fun emptyAllRatedListIsDisplayed() {
            composeTest.onNodeWithText(string.lists_rated_all_empty)
                .assertIsDisplayed()
        }

        fun emptyMoviesWatchlistIsDisplayed() {
            composeTest.onNodeWithText(string.lists_watchlist_movies_empty)
                .assertIsDisplayed()
        }

        fun emptyMoviesRatedListIsDisplayed() {
            composeTest.onNodeWithText(string.lists_rated_movies_empty)
                .assertIsDisplayed()
        }

        fun emptyTvShowsWatchlistIsDisplayed() {
            composeTest.onNodeWithText(string.lists_watchlist_tv_shows_empty)
                .assertIsDisplayed()
        }

        fun emptyTvShowsRatedListIsDisplayed() {
            composeTest.onNodeWithText(string.lists_rated_tv_shows_empty)
                .assertIsDisplayed()
        }

        fun moviesTypeIsSelected() {
            composeTest.onNodeWithText(string.item_type_movies)
                .assertIsSelected()
        }

        fun tvShowsTypeIsSelected() {
            composeTest.onNodeWithText(string.item_type_tv_shows)
                .assertIsSelected()
        }
    }

    companion object {

        fun <T : ComponentActivity> ListRobot<T>.verify(
            block: Verify<T>.() -> Unit
        ): ListRobot<T> =
            also { Verify(composeTest).block() }
    }
}

fun <T : ComponentActivity> AndroidComposeUiTest<T>.ListRobot(content: @Composable () -> Unit) =
    ListRobot(this).also { setContent(content) }
