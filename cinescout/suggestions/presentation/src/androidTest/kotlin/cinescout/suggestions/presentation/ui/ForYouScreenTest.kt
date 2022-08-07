package cinescout.suggestions.presentation.ui

import cinescout.design.TextRes
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.ui.MovieItem
import cinescout.suggestions.presentation.previewdata.ForYouMovieUiModelPreviewData
import cinescout.test.compose.robot.ForYouRobot
import cinescout.test.compose.robot.ForYouRobot.Companion.verify
import cinescout.test.compose.runComposeTest
import studio.forface.cinescout.design.R.string
import kotlin.test.Test

class ForYouScreenTest {

    @Test
    fun whenSuggestedMoviesLoading_progressIsDisplayed() = runComposeTest {
        val state = ForYouState.Loading
        ForYouRobot { ForYouScreen(state = state, actions = MovieItem.Actions.Empty) }
            .verify { progressIsDisplayed() }
    }

    @Test
    fun whenSuggestedMoviesError_messageIsDisplayed() = runComposeTest {
        val message = string.network_error_no_network
        val state = ForYouState(
            suggestedMovie = ForYouState.SuggestedMovie.Error(TextRes(message))
        )
        ForYouRobot { ForYouScreen(state = state, actions = MovieItem.Actions.Empty) }
            .verify { errorMessageIsDisplayed(message) }
    }

    @Test
    fun whenSuggestedMoviesData_movieIsDisplayed() = runComposeTest {
        val movie = ForYouMovieUiModelPreviewData.Inception
        val state = ForYouState(
            suggestedMovie = ForYouState.SuggestedMovie.Data(movie)
        )
        ForYouRobot { ForYouScreen(state = state, actions = MovieItem.Actions.Empty) }
            .verify { movieIsDisplayed(movieTitle = movie.title) }
    }
}
