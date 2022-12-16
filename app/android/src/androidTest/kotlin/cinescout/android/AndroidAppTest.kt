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
import org.junit.Ignore
import org.junit.Rule
import studio.forface.cinescout.design.R.string
import kotlin.test.BeforeTest
import kotlin.test.Test

class AndroidAppTest {

    @get:Rule
    val permissionsRule = PostNotificationsRule()

    @get:Rule
    val mockAppRule = MockAppRule()

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
    @Ignore("Implements mock rule")
    fun givenHomeIsDisplayed_whenOffline_thenOfflineErrorIsDisplayed() = runComposeAppTest {
        mockAppRule {
            offline()
        }
        homeRobot
            .verify { errorMessageIsDisplayed(TextRes(string.connection_status_services_offline)) }
    }

    @Test
    @Ignore("Implements mock rule")
    fun givenHomeIsDisplayed_whenTmdbIsNotReachable_thenTmdbErrorIsDisplayed() = runComposeAppTest {
        mockAppRule {
            tmdbNotReachable()
        }
        homeRobot
            .verify { errorMessageIsDisplayed(TextRes(string.connection_status_tmdb_offline)) }
    }

    @Test
    @Ignore("Implements mock rule")
    fun givenHomeIsDisplayed_whenTraktIsNotReachable_thenTraktErrorIsDisplayed() = runComposeAppTest {
        mockAppRule {
            traktNotReachable()
        }
        homeRobot
            .verify { errorMessageIsDisplayed(TextRes(string.connection_status_trakt_offline)) }
    }
}
