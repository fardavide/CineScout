package cinescout.details.presentation.ui

import arrow.core.none
import arrow.core.some
import cinescout.movies.domain.sample.MovieSample
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.test.compose.robot.RateMovieRobot
import cinescout.test.compose.runComposeTest
import kotlin.test.Test

internal class RateItemDialogTest {

    @Test
    fun rightTitleOfTheMovieIsDisplayed() = runComposeTest {
        // given
        val title = MovieSample.Inception.title

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
    fun whenMovieHasRating_thenCorrectRatingIsDisplayed() = runComposeTest {
        // given
        val title = MovieSample.Inception.title
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
    fun whenMovieHasNotRating_thenNoRatingIsDisplayed() = runComposeTest {
        // given
        val title = MovieSample.Inception.title

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
    fun whenRatingIsChanged_thenCorrectRatingIsDisplayed() = runComposeTest {
        // given
        val title = MovieSample.Inception.title
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
