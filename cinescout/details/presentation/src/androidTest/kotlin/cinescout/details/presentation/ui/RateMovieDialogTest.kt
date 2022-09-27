package cinescout.details.presentation.ui

import arrow.core.none
import arrow.core.some
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.getOrThrow
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.test.compose.robot.RateMovieRobot
import cinescout.test.compose.runComposeTest
import kotlin.test.Test

internal class RateMovieDialogTest {

    @Test
    fun rightTitleOfTheMovieIsDisplayed() = runComposeTest {
        // given
        val title = MovieTestData.Inception.title

        // when
        val robot = RateMovieRobot {
            RateMovieDialog(
                movieTitle = title,
                moviePersonalRating = none(),
                actions = RateMovieDialog.Actions.Empty
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
        val title = MovieTestData.Inception.title
        val rating = 9

        // when
        val robot = RateMovieRobot {
            RateMovieDialog(
                movieTitle = title,
                moviePersonalRating = Rating.of(rating).getOrThrow().some(),
                actions = RateMovieDialog.Actions.Empty
            )
        }

        // then
        robot.verify { hasRating(rating) }
    }

    @Test
    fun whenMovieHasNotRating_thenNoRatingIsDisplayed() = runComposeTest {
        // given
        val title = MovieTestData.Inception.title

        // when
        val robot = RateMovieRobot {
            RateMovieDialog(
                movieTitle = title,
                moviePersonalRating = none(),
                actions = RateMovieDialog.Actions.Empty
            )
        }

        // then
        robot.verify { hasRating(rating = 0) }
    }

    @Test
    fun whenRatingIsChanged_thenCorrectRatingIsDisplayed() = runComposeTest {
        // given
        val title = MovieTestData.Inception.title
        val rating = 9

        // when
        val robot = RateMovieRobot {
            RateMovieDialog(
                movieTitle = title,
                moviePersonalRating = Rating.of(7).getOrThrow().some(),
                actions = RateMovieDialog.Actions.Empty
            )
        }
        robot.selectRating(rating)

        // then
        robot.verify { hasRating(rating) }
    }
}
