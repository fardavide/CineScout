package cinescout.android

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import cinescout.android.testutil.PostNotificationsRule
import cinescout.android.testutil.homeRobot
import cinescout.android.testutil.runComposeAppTest
import cinescout.design.TestTag
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.test.mock.junit4.MockAppRule
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test

class AndroidAppTest {

    @get:Rule
    val permissionsRule = PostNotificationsRule()

    @get:Rule
    val mockAppRule = MockAppRule {
        newInstall()
    }

    @BeforeTest
    fun setup() {
        mockAppRule {
            watchlist {
                add(ScreenplaySample.Inception)
                add(ScreenplaySample.Dexter)
            }
        }
    }

    @Test
    fun homeScreenIsShownAtStart() = runComposeAppTest {
        onNodeWithTag(TestTag.Home)
            .assertIsDisplayed()
    }

    @Test
    fun givenHomeIsDisplayed_whenOffline_thenOfflineBannerIsDisplayed() = runComposeAppTest {
        mockAppRule {
            offline()
        }
        homeRobot
            .verify { bannerIsDisplayed(TextRes(string.connection_status_device_offline)) }
    }

    @Test
    fun givenHomeIsDisplayed_whenTmdbIsNotReachable_thenTmdbBannerIsDisplayed() = runComposeAppTest {
        mockAppRule {
            tmdbNotReachable()
        }
        homeRobot
            .verify { bannerIsDisplayed(TextRes(string.connection_status_tmdb_offline)) }
    }

    @Test
    fun givenHomeIsDisplayed_whenTraktIsNotReachable_thenTraktBannerIsDisplayed() = runComposeAppTest {
        mockAppRule {
            traktNotReachable()
        }
        homeRobot
            .verify { bannerIsDisplayed(TextRes(string.connection_status_trakt_offline)) }
    }

    @Test
    fun givenHomeIsDisplayed_whenTmdbAndTraktAreNotReachable_thenServicesBannerIsDisplayed() =
        runComposeAppTest {
            mockAppRule {
                tmdbNotReachable()
                traktNotReachable()
            }
            homeRobot
                .verify { bannerIsDisplayed(TextRes(string.connection_status_services_offline)) }
        }

    @Test
    fun givenMovieDetailsIsDisplayed_whenOffline_thenOfflineBannerIsDisplayed() = runComposeAppTest {
        mockAppRule {
            offline()
        }
        homeRobot
            .openMyLists()
            .openScreenplay(ScreenplaySample.Inception.title)
            .verify { bannerIsDisplayed(TextRes(string.connection_status_device_offline)) }
    }

    @Test
    fun givenTvShowDetailsIsDisplayed_whenOffline_thenOfflineBannerIsDisplayed() = runComposeAppTest {
        mockAppRule {
            offline()
        }
        homeRobot
            .openMyLists()
            .openScreenplay(ScreenplaySample.Dexter.title)
            .verify { bannerIsDisplayed(TextRes(string.connection_status_device_offline)) }
    }
}
