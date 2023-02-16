package cinescout.test.compose.robot

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.performClick
import cinescout.test.compose.semantic.HomeSemantics
import cinescout.test.compose.semantic.ListSemantics
import cinescout.test.compose.util.awaitDisplayed

context(ComposeUiTest, ListSemantics)
class ListRobot internal constructor() {

    fun awaitIdle(): ListRobot {
        waitForIdle()
        return this
    }

    fun openMovie(title: String): MovieDetailsRobot {
        title(title).awaitDisplayed().performClick()
        return MovieDetailsRobot()
    }

    fun openTvShow(title: String): TvShowDetailsRobot {
        title(title).awaitDisplayed().performClick()
        return TvShowDetailsRobot()
    }

    fun selectAllType(): ListRobot {
        allType().performClick()
        return this
    }

    fun selectMoviesType(): ListRobot {
        moviesType().performClick()
        return this
    }

    fun selectTvShowsType(): ListRobot {
        tvShowsType().performClick()
        return this
    }

    fun verify(block: Verify.() -> Unit): ListRobot {
        HomeSemantics { block(Verify()) }
        return this
    }

    context(ComposeUiTest, ListSemantics, HomeSemantics)
    class Verify internal constructor() : HomeRobot.Verify() {

        fun allTypeIsSelected() {
            allType().assertIsSelected()
        }

        fun dislikedScreenIsDisplayed() {
            dislikedScreen().assertIsDisplayed()
        }

        fun dislikedSubtitleIsDisplayed() {
            dislikedSubtitle().assertIsDisplayed()
        }

        fun emptyAllRatedListIsDisplayed() {
            emptyAllRated().assertIsDisplayed()
        }

        fun emptyAllWatchlistIsDisplayed() {
            emptyAllWatchlist().assertIsDisplayed()
        }

        fun emptyMoviesRatedListIsDisplayed() {
            emptyMoviesRated().assertIsDisplayed()
        }

        fun emptyMoviesWatchlistIsDisplayed() {
            emptyMoviesWatchlist().assertIsDisplayed()
        }

        fun emptyTvShowsRatedListIsDisplayed() {
            emptyTvShowsRated().assertIsDisplayed()
        }

        fun emptyTvShowsWatchlistIsDisplayed() {
            emptyTvShowsWatchlist().assertIsDisplayed()
        }

        fun likedScreenIsDisplayed() {
            likedScreen().assertIsDisplayed()
        }

        fun likedSubtitleIsDisplayed() {
            likedSubtitle().assertIsDisplayed()
        }

        fun moviesTypeIsSelected() {
            moviesType().assertIsSelected()
        }

        fun ratedScreenIsDisplayed() {
            ratedScreen().assertIsDisplayed()
        }

        fun ratedSubtitleIsDisplayed() {
            ratedSubtitle().assertIsDisplayed()
        }

        fun tvShowsTypeIsSelected() {
            tvShowsType().assertIsSelected()
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
