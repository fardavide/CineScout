package cinescout.android

import cinescout.android.testutil.forYouRobot
import cinescout.android.testutil.runComposeAppTest
import cinescout.test.mock.junit4.MockAppRule
import org.junit.Rule
import kotlin.test.Test

class ForYouFlowTest {

    @get:Rule
    val mockAppRule = MockAppRule {
        newInstall()
    }

    @Test
    fun givenIsMovieType_whenNoSuggestions_thenNoSuggestionsScreenIsDisplayed() = runComposeAppTest {
        forYouRobot
            .selectMoviesType()
            .awaitIdle()
            .verify { noMovieSuggestionsScreenIsDisplayed() }
    }

    @Test
    fun givenIsTvShowType_whenNoSuggestions_thenNoSuggestionsScreenIsDisplayed() = runComposeAppTest {
        forYouRobot
            .selectTvShowsType()
            .awaitIdle()
            .verify { noTvShowSuggestionsScreenIsDisplayed() }
    }
}
