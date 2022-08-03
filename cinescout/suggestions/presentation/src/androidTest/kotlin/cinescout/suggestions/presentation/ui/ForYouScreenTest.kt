package cinescout.suggestions.presentation.ui

import arrow.core.nonEmptyListOf
import cinescout.design.TextRes
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.test.compose.robot.ForYouRobot
import cinescout.test.compose.robot.ForYouRobot.Companion.verify
import cinescout.test.compose.runComposeTest
import studio.forface.cinescout.design.R.string
import kotlin.test.Test

class ForYouScreenTest {

    @Test
    fun whenSuggestedMoviesLoading_progressIsDisplayed() = runComposeTest {
        val state = ForYouState.Loading
        ForYouRobot { ForYouScreen(state = state) }
            .verify { progressIsDisplayed() }
    }

    @Test
    fun whenSuggestedMoviesError_messageIsDisplayed() = runComposeTest {
        val message = string.network_error_no_network
        val state = ForYouState(
            suggestedMovies = ForYouState.SuggestedMovies.Error(TextRes(message))
        )
        ForYouRobot { ForYouScreen(state = state) }
            .verify { errorMessageIsDisplayed(message) }
    }

    @Test
    fun whenSuggestedMoviesData_movieIsDisplayed() = runComposeTest {
        val movie = MovieTestData.Inception
        val state = ForYouState(
            suggestedMovies = ForYouState.SuggestedMovies.Data(nonEmptyListOf(movie))
        )
        ForYouRobot { ForYouScreen(state = state) }
            .verify { movieIsDisplayed(movieTitle = movie.title) }
    }
}
