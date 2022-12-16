package cinescout.android

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import cinescout.android.testutil.PostNotificationsRule
import cinescout.android.testutil.homeRobot
import cinescout.android.testutil.runComposeAppTest
import cinescout.design.TestTag
import cinescout.design.TextRes
import cinescout.suggestions.presentation.ui.ForYouTypeSelector
import cinescout.test.compose.robot.HomeRobot.Companion.verify
import cinescout.test.mock.MockAppRule
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test

class AndroidAppTest {

    @get:Rule
    val permissionsRule = PostNotificationsRule()

    @get:Rule
    val mockApRule = MockAppRule()

    @BeforeTest
    fun setup() {
        ForYouTypeSelector.animateChanges = false
    }

    @Test
    fun homeScreenIsShownAtStart() = runComposeAppTest {
        onNodeWithTag(TestTag.Home)
            .assertIsDisplayed()
    }

    @Test
    fun givenHomeIsDisplayed_whenOffline_thenOfflineErrorIsDisplayed() = runComposeAppTest {
        mockApRule {
            offline()
        }
        homeRobot
            .verify { errorMessageIsDisplayed(TextRes("You're offline")) }
    }

    @Test
    fun givenHomeIsDisplayed_whenTmdbIsNotReachable_thenTmdbErrorIsDisplayed() = runComposeAppTest {
        mockApRule {
            tmdbNotReachable()
        }
        homeRobot
            .verify { errorMessageIsDisplayed(TextRes("Tmdb is not reachable")) }
    }

    @Test
    fun givenHomeIsDisplayed_whenTraktIsNotReachable_thenTraktErrorIsDisplayed() = runComposeAppTest {
        mockApRule {
            traktNotReachable()
        }
        homeRobot
            .verify { errorMessageIsDisplayed(TextRes("Trakt is not reachable")) }
    }
}
