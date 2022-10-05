package cinescout.suggestions.presentation.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import cinescout.design.TestTag
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
    fun whenNoSuggestions_errorIsShown() = runComposeTest {
        val state = ForYouState(
            shouldShowHint = false,
            suggestedMovie = ForYouState.SuggestedMovie.NoSuggestions
        )
        ForYouRobot { ForYouScreen(state = state) }
            .verify { errorMessageIsDisplayed(string.suggestions_no_suggestions) }
    }

    @Test
    fun whenNoSuggestions_searchLikedScreenIsShown() = runComposeTest {
        val state = ForYouState(
            shouldShowHint = false,
            suggestedMovie = ForYouState.SuggestedMovie.NoSuggestions
        )
        ForYouRobot { ForYouScreen(state = state) }
            .verify { searchLikedIsDisplayed() }
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
            shouldShowHint = false,
            suggestedMovie = ForYouState.SuggestedMovie.Error(TextRes(message))
        )
        ForYouRobot { ForYouScreen(state = state) }
            .verify { errorMessageIsDisplayed(message) }
    }

    @Test
    fun whenSuggestedMoviesData_movieIsDisplayed() = runComposeTest {
        val movie = ForYouMovieUiModelPreviewData.Inception
        val state = ForYouState(
            shouldShowHint = false,
            suggestedMovie = ForYouState.SuggestedMovie.Data(movie)
        )
        ForYouRobot { ForYouScreen(state = state) }
            .verify { movieIsDisplayed(movieTitle = movie.title) }
    }

    @Composable
    private fun ForYouScreen(state: ForYouState) {
        ForYouScreen(
            state = state,
            actions = ForYouScreen.Actions.Empty,
            itemActions = ForYouMovieItem.Actions.Empty,
            searchLikedMovieScreen = {
                Text(modifier = Modifier.testTag(TestTag.SearchLiked), text = "No suggestions")
            }
        )
    }
}
