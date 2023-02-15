package cinescout.test.compose.robot

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import cinescout.design.R.string
import cinescout.test.compose.semantic.ForYouSemantics
import cinescout.test.compose.semantic.HomeSemantics
import cinescout.test.compose.util.awaitDisplayed
import cinescout.test.compose.util.onNodeWithText

context(ComposeUiTest, ForYouSemantics)
class ForYouRobot internal constructor() {

    fun awaitIdle(): ForYouRobot {
        waitForIdle()
        return this
    }

    fun openMovieDetails(): MovieDetailsRobot {
        onNodeWithText(string.suggestions_for_you_open_details)
            .awaitDisplayed()
            .performClick()

        return MovieDetailsRobot()
    }
    
    fun openTvShowDetails(): TvShowDetailsRobot {
        onNodeWithText(string.suggestions_for_you_open_details)
            .awaitDisplayed()
            .performClick()

        return TvShowDetailsRobot()
    }

    fun performLikeAction(fraction: Float = 1f, performUp: Boolean = true): ForYouRobot {
        onRoot()
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

    fun selectMoviesType(): ForYouRobot {
        onNodeWithText(string.item_type_movies)
            .performClick()
        return this
    }

    fun selectTvShowsType(): ForYouRobot {
        onNodeWithText(string.item_type_tv_shows)
            .performClick()
        return this
    }

    fun verify(block: Verify.() -> Unit): ForYouRobot {
        HomeSemantics { block(Verify()) }
        return this
    }

    context(ComposeUiTest, ForYouSemantics, HomeSemantics)
    class Verify internal constructor() : HomeRobot.Verify() {

        fun movieIsDisplayed(movieTitle: String) {
            onNodeWithText(movieTitle)
                .assertIsDisplayed()
        }

        fun moviesTypeIsSelected() {
            onNodeWithText(string.item_type_movies)
                .assertIsSelected()
        }

        fun noMovieSuggestionsScreenIsDisplayed() {
            onNodeWithText(string.suggestions_no_movie_suggestions)
                .assertIsDisplayed()
        }

        fun noTvShowSuggestionsScreenIsDisplayed() {
            onNodeWithText(string.suggestions_no_tv_show_suggestions)
                .assertIsDisplayed()
        }

        fun screenIsDisplayed() {
            screen().assertIsDisplayed()
        }

        fun subtitleIsDisplayed() {
            subtitle().assertIsDisplayed()
        }

        fun tvShowIsDisplayed(tvShowTitle: String) {
            onNodeWithText(tvShowTitle)
                .assertIsDisplayed()
        }

        fun tvShowsTypeIsSelected() {
            onNodeWithText(string.item_type_tv_shows)
                .assertIsSelected()
        }
    }
}

context(ComposeUiTest)
fun ForYouRobot(content: @Composable () -> Unit): ForYouRobot {
    setContent(content)
    return ForYouSemantics { ForYouRobot() }
}
