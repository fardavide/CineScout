package cinescout.android

import cinescout.android.testutil.PostNotificationsRule
import cinescout.android.testutil.homeRobot
import cinescout.android.testutil.runComposeAppTest
import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.suggestions.domain.sample.SuggestedScreenplaySample
import cinescout.test.mock.junit4.MockAppRule
import org.junit.Rule
import kotlin.test.Test

class NavigationFlowTest {

    @get:Rule
    val appRule = MockAppRule {
        newInstall()
        updatedCache()
    }

    @get:Rule
    val permissionsRule = PostNotificationsRule()

    // region given home is displayed
    @Test
    fun givenHomeIsDisplayed_whenForYouIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openForYou()
            .verify {
                screenIsDisplayed()
                subtitleIsDisplayed()
            }
    }

    @Test
    fun givenHomeIsDisplayed_whenMyListsIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openMyLists()
            .verify {
                screenIsDisplayed()
                titleIsDisplayed()
            }
    }

    @Test
    fun givenHomeIsDisplayed_whenProfileIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openProfile()
            .verify {
                screenIsDisplayed()
                titleIsDisplayed()
            }
    }
    // endregion

    // region given for you is displayed
    @Test
    fun givenForYouIsDisplayed_whenMovieIsSelected_detailsIsDisplayed() {
        appRule {
            newInstall()
            updatedCache()

            forYou {
                movie(SuggestedScreenplaySample.Inception)
            }
        }

        runComposeAppTest {
            homeRobot
                .openForYou()
                .selectMoviesType()
                .openMovieDetails()
                .verify { movieDetailsIsDisplayed() }
        }
    }

    @Test
    fun givenForYouIsDisplayed_whenTvShowIsSelected_detailsIsDisplayed() {
        appRule {
            newInstall()
            updatedCache()

            forYou {
                tvShow(SuggestedScreenplaySample.Grimm)
            }
        }

        runComposeAppTest {
            homeRobot
                .openForYou()
                .selectTvShowsType()
                .awaitIdle()
                .openTvShowDetails()
                .verify { tvShowDetailsIsDisplayed() }
        }
    }
    // endregion

    // region given my lists is displayed
    @Test
    fun givenMyListsIsDisplayed_whenMovieIsSelected_detailsIsDisplayed() {
        appRule {
            watchlist {
                movie(ScreenplaySample.War)
            }
        }

        runComposeAppTest {
            homeRobot
                .openMyLists()
                .openMovie(ScreenplaySample.War.title)
                .verify { movieDetailsIsDisplayed() }
        }
    }

    @Test
    fun givenMyListsIsDisplayed_whenTvShowIsSelected_detailsIsDisplayed() {
        appRule {
            watchlist {
                tvShow(ScreenplaySample.BreakingBad)
            }
        }

        runComposeAppTest {
            homeRobot
                .openMyLists()
                .openTvShow(ScreenplaySample.BreakingBad.title)
                .verify { tvShowDetailsIsDisplayed() }
        }
    }
    // endregion

    // region given profile is displayed
    @Test
    fun givenProfileIsDisplayed_whenManageAccountIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openProfile()
            .openManageAccount()
            .verify {
                screenIsDisplayed()
                titleIsDisplayed()
            }
    }
    // endregion
}
