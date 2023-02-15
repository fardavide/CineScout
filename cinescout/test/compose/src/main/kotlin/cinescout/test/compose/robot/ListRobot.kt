package cinescout.test.compose.robot

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import cinescout.design.R.string
import cinescout.test.compose.semantic.HomeSemantics
import cinescout.test.compose.semantic.ListSemantics
import cinescout.test.compose.util.awaitDisplayed
import cinescout.test.compose.util.onNodeWithText

context(ComposeUiTest, ListSemantics)
class ListRobot internal constructor() {

    fun awaitIdle(): ListRobot {
        waitForIdle()
        return this
    }

    fun openMovie(title: String): MovieDetailsRobot {
        onNodeWithText(title)
            .awaitDisplayed()
            .performClick()
        return MovieDetailsRobot()
    }

    fun openTvShow(title: String): TvShowDetailsRobot {
        onNodeWithText(title).performClick()
        return TvShowDetailsRobot()
    }

    fun selectAllType(): ListRobot {
        onNodeWithText(string.item_type_all).performClick()
        return this
    }

    fun selectMoviesType(): ListRobot {
        onNodeWithText(string.item_type_movies).performClick()
        return this
    }

    fun selectTvShowsType(): ListRobot {
        onNodeWithText(string.item_type_tv_shows).performClick()
        return this
    }

    fun verify(block: Verify.() -> Unit): ListRobot {
        HomeSemantics { block(Verify()) }
        return this
    }

    context(ComposeUiTest, ListSemantics, HomeSemantics)
    class Verify internal constructor() : HomeRobot.Verify() {

        fun allTypeIsSelected() {
            onNodeWithText(string.item_type_all).assertIsSelected()
        }

        fun dislikedScreenIsDisplayed() {
            dislikedScreen().assertIsDisplayed()
        }

        fun dislikedSubtitleIsDisplayed() {
            dislikedSubtitle().assertIsDisplayed()
        }

        fun emptyAllWatchlistIsDisplayed() {
            onNodeWithText(string.lists_watchlist_all_empty).assertIsDisplayed()
        }

        fun emptyAllRatedListIsDisplayed() {
            onNodeWithText(string.lists_rated_all_empty).assertIsDisplayed()
        }

        fun emptyMoviesWatchlistIsDisplayed() {
            onNodeWithText(string.lists_watchlist_movies_empty).assertIsDisplayed()
        }

        fun emptyMoviesRatedListIsDisplayed() {
            onNodeWithText(string.lists_rated_movies_empty).assertIsDisplayed()
        }

        fun emptyTvShowsWatchlistIsDisplayed() {
            onNodeWithText(string.lists_watchlist_tv_shows_empty).assertIsDisplayed()
        }

        fun emptyTvShowsRatedListIsDisplayed() {
            onNodeWithText(string.lists_rated_tv_shows_empty).assertIsDisplayed()
        }

        fun likedScreenIsDisplayed() {
            likedScreen().assertIsDisplayed()
        }

        fun likedSubtitleIsDisplayed() {
            likedSubtitle().assertIsDisplayed()
        }

        fun moviesTypeIsSelected() {
            onNodeWithText(string.item_type_movies).assertIsSelected()
        }

        fun ratedScreenIsDisplayed() {
            ratedScreen().assertIsDisplayed()
        }

        fun ratedSubtitleIsDisplayed() {
            ratedSubtitle().assertIsDisplayed()
        }

        fun tvShowsTypeIsSelected() {
            onNodeWithText(string.item_type_tv_shows).assertIsSelected()
        }

        fun watchlistScreenIsDisplayed() {
            watchlistScreen().assertIsDisplayed()
        }

        fun watchlistSubtitleIsDisplayed() {
            watchlistSubtitle().assertIsDisplayed()
        }
    }
}

context(ComposeUiTest)
fun ListRobot(content: @Composable () -> Unit): ListRobot {
    setContent(content)
    return ListSemantics { ListRobot() }
}
