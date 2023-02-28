package cinescout.android

import cinescout.android.testutil.PostNotificationsRule
import cinescout.android.testutil.homeRobot
import cinescout.android.testutil.runComposeAppTest
import cinescout.movies.domain.sample.MovieSample
import cinescout.test.mock.junit4.MockAppRule
import cinescout.tvshows.domain.sample.TvShowSample
import org.junit.Rule
import kotlin.test.Ignore
import kotlin.test.Test

class NavigationFlowTest {

    @get:Rule
    val appRule = MockAppRule {
        newInstall()
        updatedCache()
    }

    @get:Rule
    val permissionsRule = PostNotificationsRule()

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
    fun givenForYouIsDisplayed_whenMovieIsSelected_detailsIsDisplayed() {
        appRule {
            newInstall()
            updatedCache()

            forYou {
                movie(MovieSample.Inception)
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
    @Ignore("Flaky when run with other tests")
    fun givenForYouIsDisplayed_whenTvShowIsSelected_detailsIsDisplayed() {
        appRule {
            newInstall()
            updatedCache()

            forYou {
                tvShow(TvShowSample.Grimm)
            }
        }

        runComposeAppTest {
            homeRobot
                .openForYou()
                .selectTvShowsType()
                .openTvShowDetails()
                .verify { tvShowDetailsIsDisplayed() }
        }
    }

    @Test
    fun givenMyListsIsDisplayed_whenMovieIsSelected_detailsIsDisplayed() {
        appRule {
            watchlist {
                movie(MovieSample.War)
            }
        }

        runComposeAppTest {
            homeRobot
                .openMyLists()
                .openMovie(MovieSample.War.title)
                .verify { movieDetailsIsDisplayed() }
        }
    }

    @Test
    fun givenMyListsIsDisplayed_whenTvShowIsSelected_detailsIsDisplayed() {
        appRule {
            watchlist {
                tvShow(TvShowSample.BreakingBad)
            }
        }

        runComposeAppTest {
            homeRobot
                .openMyLists()
                .openTvShow(TvShowSample.BreakingBad.title)
                .verify { tvShowDetailsIsDisplayed() }
        }
    }
}
