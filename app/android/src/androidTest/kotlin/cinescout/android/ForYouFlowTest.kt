package cinescout.android

import cinescout.android.testutil.forYouRobot
import cinescout.android.testutil.runComposeAppTest
import cinescout.test.compose.robot.ForYouRobot.Companion.verify
import cinescout.test.mock.MockAppRule
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
            .verify { noMovieSuggestionsScreenIsDisplayed() }
    }

    @Test
    fun givenIsTvShowType_whenNoSuggestions_thenNoSuggestionsScreenIsDisplayed() = runComposeAppTest {
        forYouRobot
            .selectTvShowsType()
            .verify { noTvShowSuggestionsScreenIsDisplayed() }
    }
}
