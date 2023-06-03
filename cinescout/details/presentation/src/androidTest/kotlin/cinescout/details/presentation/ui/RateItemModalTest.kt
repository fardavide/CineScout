package cinescout.details.presentation.ui

import androidx.compose.ui.test.runComposeUiTest
import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.test.compose.robot.RateMovieRobot
import kotlin.test.Test

internal class RateItemModalTest {

    @Test
    fun rightTitleOfTheMovieIsDisplayed() = runComposeUiTest {
        // given
        val title = ScreenplaySample.Inception.title

        // when
        val robot = RateMovieRobot {
            RateItemModal(
                itemTitle = title,
                itemPersonalRating = 0,
                actions = RateItemModal.Actions.Empty
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
            RateItemModal(
                itemTitle = title,
                itemPersonalRating = rating,
                actions = RateItemModal.Actions.Empty
            )
        }

        // then
        robot.verify { hasRating(rating) }
    }

    @Test
    fun whenMovieHasNoRating_thenNoRatingIsDisplayed() = runComposeUiTest {
        // given
        val title = ScreenplaySample.Inception.title

        // when
        val robot = RateMovieRobot {
            RateItemModal(
                itemTitle = title,
                itemPersonalRating = 0,
                actions = RateItemModal.Actions.Empty
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
            RateItemModal(
                itemTitle = title,
                itemPersonalRating = 7,
                actions = RateItemModal.Actions.Empty
            )
        }
        robot.selectRating(rating)

        // then
        robot.verify { hasRating(rating) }
    }
}
