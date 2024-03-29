package cinescout.suggestions.presentation.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.runComposeUiTest
import cinescout.design.TestTag
import cinescout.design.theme.Dimens
import cinescout.resources.sample.MessageSample
import cinescout.suggestions.presentation.model.ForYouType
import cinescout.suggestions.presentation.sample.ForYouScreenplayUiModelSample
import cinescout.suggestions.presentation.state.ForYouState
import cinescout.test.compose.robot.ForYouRobot
import kotlin.test.Test

class ForYouScreenTest {

    @Test
    fun whenError_messageIsShown() = runComposeUiTest {
        val message = MessageSample.NoNetworkError
        val state = ForYouState(
            suggestedItem = ForYouState.SuggestedItem.Error(message),
            type = ForYouType.Movies
        )
        ForYouRobot { ForYouScreen(state = state) }
            .verify { errorMessageIsDisplayed(message) }
    }

    @Test
    fun givenTypeIsMovies_whenNoSuggestions_searchLikedScreenIsShown() = runComposeUiTest {
        val state = ForYouState(
            suggestedItem = ForYouState.SuggestedItem.NoSuggestedMovies,
            type = ForYouType.Movies
        )
        ForYouRobot { ForYouScreen(state = state) }
            .verify { searchLikedIsDisplayed() }
    }

    @Test
    fun givenTypeIsTvShows_whenNoSuggestions_searchLikedScreenIsShown() = runComposeUiTest {
        val state = ForYouState(
            suggestedItem = ForYouState.SuggestedItem.NoSuggestedTvShows,
            type = ForYouType.TvShows
        )
        ForYouRobot { ForYouScreen(state = state) }
            .verify { searchLikedIsDisplayed() }
    }

    @Test
    fun whenSuggestionsAreLoading_progressIsDisplayed() = runComposeUiTest {
        val state = ForYouState.Loading
        ForYouRobot { ForYouScreen(state = state) }
            .verify { progressIsDisplayed() }
    }

    @Test
    fun whenSuggestedMoviesData_movieIsDisplayed() = runComposeUiTest {
        val movie = ForYouScreenplayUiModelSample.Inception
        val state = ForYouState(
            suggestedItem = ForYouState.SuggestedItem.Screenplay(movie),
            type = ForYouType.Movies
        )
        ForYouRobot { ForYouScreen(state = state) }
            .verify { movieIsDisplayed(movieTitle = movie.title) }
    }

    @Test
    fun whenSuggestedTvShowsData_tvShowIsDisplayed() = runComposeUiTest {
        val tvShow = ForYouScreenplayUiModelSample.Grimm
        val state = ForYouState(
            suggestedItem = ForYouState.SuggestedItem.Screenplay(tvShow),
            type = ForYouType.TvShows
        )
        ForYouRobot { ForYouScreen(state = state) }
            .verify { tvShowIsDisplayed(tvShowTitle = tvShow.title) }
    }

    @Composable
    private fun ForYouScreen(state: ForYouState) {
        ForYouScreen(
            state = state,
            verticalSpacing = Dimens.Margin.small,
            itemActions = ForYouItem.Actions.Empty,
            buttonsActions = ForYouButtons.Actions.Empty,
            selectType = {}
        ) {
            Text(modifier = Modifier.testTag(TestTag.SearchLiked), text = "No suggestions")
        }
    }
}
