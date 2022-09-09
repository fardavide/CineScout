package cinescout.suggestions.presentation.ui

import androidx.compose.runtime.Composable
import cinescout.design.TextRes
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.previewdata.ForYouMovieUiModelPreviewData
import cinescout.test.compose.robot.ForYouRobot
import cinescout.test.compose.robot.ForYouRobot.Companion.verify
import cinescout.test.compose.runComposeTest
import studio.forface.cinescout.design.R.string
import kotlin.test.Test

class ForYouScreenTest {

    @Test
    fun whenNotLoggedIn_correctMessageIsShown() = runComposeTest {
        val state = ForYouState(
            loggedIn = ForYouState.LoggedIn.False,
            suggestedMovie = ForYouState.SuggestedMovie.NoSuggestions
        )
        ForYouRobot { ForYouScreen(state = state) }
            .verify { errorMessageIsDisplayed(string.suggestions_for_you_not_logged_in) }
    }

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
            loggedIn = ForYouState.LoggedIn.True,
            suggestedMovie = ForYouState.SuggestedMovie.Error(TextRes(message))
        )
        ForYouRobot { ForYouScreen(state = state) }
            .verify { errorMessageIsDisplayed(message) }
    }

    @Test
    fun whenSuggestedMoviesData_movieIsDisplayed() = runComposeTest {
        val movie = ForYouMovieUiModelPreviewData.Inception
        val state = ForYouState(
            loggedIn = ForYouState.LoggedIn.True,
            suggestedMovie = ForYouState.SuggestedMovie.Data(movie)
        )
        ForYouRobot { ForYouScreen(state = state) }
            .verify { movieIsDisplayed(movieTitle = movie.title) }
    }

    @Composable
    private fun ForYouScreen(state: ForYouState) {
        ForYouScreen(state = state, actions = ForYouScreen.Actions.Empty, itemActions = ForYouMovieItem.Actions.Empty)
    }
}
