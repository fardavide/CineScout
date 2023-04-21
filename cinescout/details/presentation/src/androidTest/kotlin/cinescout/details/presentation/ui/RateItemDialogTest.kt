package cinescout.details.presentation.ui

import androidx.compose.ui.test.runComposeUiTest
import arrow.core.none
import arrow.core.some
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.test.compose.robot.RateMovieRobot
import kotlin.test.Test

internal class RateItemDialogTest {

    @Test
    fun rightTitleOfTheMovieIsDisplayed() = runComposeUiTest {
        // given
        val title = ScreenplaySample.Inception.title

        // when
        val robot = RateMovieRobot {
            RateItemDialog(
                itemTitle = title,
                itemPersonalRating = none(),
                actions = RateItemDialog.Actions.Empty
            )
        }

        // then
        robot.verify {
            hasTitle(title)
        }
    }

    @Test
    fun whenMovieHasRating_thenCorrectRatingIsDisplayed() = runComposeUiTest {
        // given
        val title = ScreenplaySample.Inception.title
        val rating = 9

        // when
        val robot = RateMovieRobot {
            RateItemDialog(
                itemTitle = title,
                itemPersonalRating = Rating.of(rating).getOrThrow().some(),
                actions = RateItemDialog.Actions.Empty
            )
        }

        // then
        robot.verify { hasRating(rating) }
    }

    @Test
    fun whenMovieHasNotRating_thenNoRatingIsDisplayed() = runComposeUiTest {
        // given
        val title = ScreenplaySample.Inception.title

        // when
        val robot = RateMovieRobot {
            RateItemDialog(
                itemTitle = title,
                itemPersonalRating = none(),
                actions = RateItemDialog.Actions.Empty
            )
        }

        // then
        robot.verify { hasRating(rating = 0) }
    }

    @Test
    fun whenRatingIsChanged_thenCorrectRatingIsDisplayed() = runComposeUiTest {
        // given
        val title = ScreenplaySample.Inception.title
        val rating = 9

        // when
        val robot = RateMovieRobot {
            RateItemDialog(
                itemTitle = title,
                itemPersonalRating = Rating.of(7).getOrThrow().some(),
                actions = RateItemDialog.Actions.Empty
            )
        }
        robot.selectRating(rating)

        // then
        robot.verify { hasRating(rating) }
    }
}
