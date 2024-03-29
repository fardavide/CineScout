package cinescout.android

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
    }

    // region given home is displayed
    @Test
    fun givenHomeIsDisplayed_whenForYouIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openForYou()
            .verify {
                screenIsDisplayed()
                titleIsDisplayed()
            }
    }

    @Test
    fun givenHomeIsDisplayed_whenSearchIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openSearch()
            .verify {
                screenIsDisplayed()
                titleIsDisplayed()
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

            forYou {
                movie(SuggestedScreenplaySample.Avatar3)
            }
        }

        runComposeAppTest {
            homeRobot
                .openForYou()
                .selectMoviesType()
                .awaitIdle()
                .awaitScreenplay(ScreenplaySample.Avatar3.title)
                .openDetails()
                .verify { detailsIsDisplayed() }
        }
    }

    @Test
    fun givenForYouIsDisplayed_whenTvShowIsSelected_detailsIsDisplayed() {
        appRule {
            newInstall()

            forYou {
                tvShow(SuggestedScreenplaySample.Grimm)
            }
        }

        runComposeAppTest {
            homeRobot
                .openForYou()
                .selectTvShowsType()
                .awaitIdle()
                .awaitScreenplay(ScreenplaySample.Grimm.title)
                .openDetails()
                .verify { detailsIsDisplayed() }
        }
    }
    // endregion

    // region given my lists is displayed
    @Test
    fun givenMyListsIsDisplayed_whenMovieIsSelected_detailsIsDisplayed() {
        appRule {
            watchlist {
                add(ScreenplaySample.War)
            }
        }

        runComposeAppTest {
            homeRobot
                .openMyLists()
                .openScreenplay(ScreenplaySample.War.title)
                .verify { detailsIsDisplayed() }
        }
    }

    @Test
    fun givenMyListsIsDisplayed_whenTvShowIsSelected_detailsIsDisplayed() {
        appRule {
            watchlist {
                add(ScreenplaySample.BreakingBad)
            }
        }

        runComposeAppTest {
            homeRobot
                .openMyLists()
                .openScreenplay(ScreenplaySample.BreakingBad.title)
                .verify { detailsIsDisplayed() }
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

    @Test
    fun givenProfileIsDisplayed_whenSettingsIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openProfile()
            .openSettings()
            .verify {
                screenIsDisplayed()
                titleIsDisplayed()
            }
    }

    @Test
    fun givenProfileIsDisplayed_whenReportBugIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openProfile()
            .openReportBug()
            .verify {
                screenIsDisplayed()
                titleIsDisplayed()
            }
    }

    @Test
    fun givenProfileIsDisplayed_whenRequestFeatureIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openProfile()
            .openRequestFeature()
            .verify {
                screenIsDisplayed()
                titleIsDisplayed()
            }
    }
    // endregion
}
