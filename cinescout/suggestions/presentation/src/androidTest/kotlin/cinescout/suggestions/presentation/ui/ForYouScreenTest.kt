package cinescout.suggestions.presentation.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import cinescout.design.TestTag
import cinescout.design.testdata.MessageSample
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.model.ForYouType
import cinescout.suggestions.presentation.sample.ForYouMovieUiModelSample
import cinescout.suggestions.presentation.sample.ForYouTvShowUiModelSample
import cinescout.suggestions.presentation.util.Stack
import cinescout.test.compose.robot.ForYouRobot
import cinescout.test.compose.robot.ForYouRobot.Companion.verify
import cinescout.test.compose.runComposeTest
import kotlin.test.Test

class ForYouScreenTest {

    @Test
    fun whenError_messageIsShown() = runComposeTest {
        val message = MessageSample.NoNetworkError
        val state = ForYouState(
            suggestedItem = ForYouState.SuggestedItem.Error(message),
            moviesStack = Stack.empty(),
            tvShowsStack = Stack.empty(),
            type = ForYouType.Movies
        )
        ForYouRobot { ForYouScreen(state = state) }
            .verify { errorMessageIsDisplayed(message) }
    }

    @Test
    fun givenTypeIsMovies_whenNoSuggestions_searchLikedScreenIsShown() = runComposeTest {
        val state = ForYouState(
            suggestedItem = ForYouState.SuggestedItem.NoSuggestedMovies,
            moviesStack = Stack.empty(),
            tvShowsStack = Stack.empty(),
            type = ForYouType.Movies
        )
        ForYouRobot { ForYouScreen(state = state) }
            .verify { searchLikedIsDisplayed() }
    }

    @Test
    fun givenTypeIsTvShows_whenNoSuggestions_searchLikedScreenIsShown() = runComposeTest {
        val state = ForYouState(
            suggestedItem = ForYouState.SuggestedItem.NoSuggestedTvShows,
            moviesStack = Stack.empty(),
            tvShowsStack = Stack.empty(),
            type = ForYouType.TvShows
        )
        ForYouRobot { ForYouScreen(state = state) }
            .verify { searchLikedIsDisplayed() }
    }

    @Test
    fun whenSuggestionsAreLoading_progressIsDisplayed() = runComposeTest {
        val state = ForYouState.Loading
        ForYouRobot { ForYouScreen(state = state) }
            .verify { progressIsDisplayed() }
    }

    @Test
    fun whenSuggestedMoviesData_movieIsDisplayed() = runComposeTest {
        val movie = ForYouMovieUiModelSample.Inception
        val state = ForYouState(
            suggestedItem = ForYouState.SuggestedItem.Screenplay(movie),
            moviesStack = Stack.empty(),
            tvShowsStack = Stack.empty(),
            type = ForYouType.Movies
        )
        ForYouRobot { ForYouScreen(state = state) }
            .verify { movieIsDisplayed(movieTitle = movie.title) }
    }

    @Test
    fun whenSuggestedTvShowsData_tvShowIsDisplayed() = runComposeTest {
        val tvShow = ForYouTvShowUiModelSample.Grimm
        val state = ForYouState(
            suggestedItem = ForYouState.SuggestedItem.Screenplay(tvShow),
            moviesStack = Stack.empty(),
            tvShowsStack = Stack.empty(),
            type = ForYouType.TvShows
        )
        ForYouRobot { ForYouScreen(state = state) }
            .verify { tvShowIsDisplayed(tvShowTitle = tvShow.title) }
    }

    @Composable
    private fun ForYouScreen(state: ForYouState) {
        ForYouScreen(
            state = state,
            itemActions = ForYouItem.Actions.Empty,
            buttonsActions = ForYouButtons.Actions.Empty,
            selectType = {}
        ) {
            Text(modifier = Modifier.testTag(TestTag.SearchLiked), text = "No suggestions")
        }
    }
}
