package cinescout.android

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import cinescout.android.testutil.PostNotificationsRule
import cinescout.android.testutil.homeRobot
import cinescout.android.testutil.runComposeAppTest
import cinescout.design.TestTag
import cinescout.design.TextRes
import cinescout.lists.presentation.ui.ListTypeSelector
import cinescout.movies.domain.sample.MovieSample
import cinescout.suggestions.presentation.ui.ForYouTypeSelector
import cinescout.test.compose.robot.HomeRobot.Companion.verify
import cinescout.test.mock.MockAppRule
import cinescout.tvshows.domain.sample.TvShowSample
import org.junit.Rule
import studio.forface.cinescout.design.R.string
import kotlin.test.BeforeTest
import kotlin.test.Test

class AndroidAppTest {

    @get:Rule
    val permissionsRule = PostNotificationsRule()

    @get:Rule
    val mockAppRule = MockAppRule {
        watchlist {
            movie(MovieSample.Inception)
            tvShow(TvShowSample.Dexter)
        }
    }

    @BeforeTest
    fun setup() {
        ForYouTypeSelector.animateChanges = false
        ListTypeSelector.animateChanges = false
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
    fun givenHomeIsDisplayed_whenTmdbAndTraktAreNotReachable_thenServicesBannerIsDisplayed() = runComposeAppTest {
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
            .openDrawer()
            .openWatchlist()
            .openMovie(MovieSample.Inception.title)
            .verify { bannerIsDisplayed(TextRes(string.connection_status_device_offline)) }
    }

    @Test
    fun givenTvShowDetailsIsDisplayed_whenOffline_thenOfflineBannerIsDisplayed() = runComposeAppTest {
        mockAppRule {
            offline()
        }
        homeRobot
            .openDrawer()
            .openWatchlist()
            .openTvShow(TvShowSample.Dexter.title)
            .verify { bannerIsDisplayed(TextRes(string.connection_status_device_offline)) }
    }
}
