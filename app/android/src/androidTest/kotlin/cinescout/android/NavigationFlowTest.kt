package cinescout.android

import cinescout.android.testutil.PostNotificationsRule
import cinescout.android.testutil.homeRobot
import cinescout.android.testutil.runComposeAppTest
import cinescout.common.model.Rating
import cinescout.lists.presentation.ui.ListTypeSelector
import cinescout.movies.domain.sample.MovieSample
import cinescout.suggestions.presentation.ui.ForYouTypeSelector
import cinescout.test.mock.MockAppRule
import cinescout.tvshows.domain.sample.TvShowSample
import org.junit.Rule
import kotlin.test.BeforeTest
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

    @BeforeTest
    fun setup() {
        ListTypeSelector.animateChanges = false
        ForYouTypeSelector.animateChanges = false
    }

    @Test
    fun givenHomeIsDisplayed_whenForYouIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openForYou()
            .verify {
                screenIsDisplayed()
                subtitleIsDisplayed()
            }
    }

    @Test
    fun givenHomeIsDisplayed_whenMyListsIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openMyLists()
            .verify {
                screenIsDisplayed()
                subtitleIsDisplayed()
            }
    }

    @Test
    fun givenHomeIsDisplayed_whenWatchlistIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openWatchlist()
            .verify {
                watchlistScreenIsDisplayed()
                watchlistSubtitleIsDisplayed()
            }
    }

    @Test
    @Ignore("Flaky when run with other tests")
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
                .asForYou()
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
                .asForYou()
                .selectTvShowsType()
                .openTvShowDetails()
                .verify { tvShowDetailsIsDisplayed() }
        }
    }

    @Test
    fun givenMyListsIsDisplayed_whenDislikedIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openMyLists()
            .openDisliked()
            .verify {
                dislikedScreenIsDisplayed()
                dislikedSubtitleIsDisplayed()
            }
    }

    @Test
    fun givenMyListsIsDisplayed_whenLikedIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openMyLists()
            .openLiked()
            .verify {
                likedScreenIsDisplayed()
                likedSubtitleIsDisplayed()
            }
    }

    @Test
    fun givenMyListsIsDisplayed_whenRatedIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openMyLists()
            .openRated()
            .verify {
                ratedScreenIsDisplayed()
                ratedSubtitleIsDisplayed()
            }
    }

    @Test
    fun givenDislikedListIsDisplayed_whenMovieIsSelected_detailsIsDisplayed() {
        appRule {
            disliked {
                movie(MovieSample.War)
            }
        }

        runComposeAppTest {
            homeRobot
                .openDrawer()
                .openMyLists()
                .openDisliked()
                .openMovie(MovieSample.War.title)
                .verify { movieDetailsIsDisplayed() }
        }
    }

    @Test
    fun givenDislikedListIsDisplayed_whenTvShowIsSelected_detailsIsDisplayed() {
        appRule {
            disliked {
                tvShow(TvShowSample.BreakingBad)
            }
        }

        runComposeAppTest {
            homeRobot
                .openDrawer()
                .openMyLists()
                .openDisliked()
                .openTvShow(TvShowSample.BreakingBad.title)
                .verify { tvShowDetailsIsDisplayed() }
        }
    }

    @Test
    fun givenLikedListIsDisplayed_whenMovieIsSelected_detailsIsDisplayed() {
        appRule {
            liked {
                movie(MovieSample.Inception)
            }
        }

        runComposeAppTest {
            homeRobot
                .openDrawer()
                .openMyLists()
                .openLiked()
                .openMovie(MovieSample.Inception.title)
                .verify { movieDetailsIsDisplayed() }
        }
    }

    @Test
    fun givenLikedListIsDisplayed_whenTvShowIsSelected_detailsIsDisplayed() {
        appRule {
            liked {
                tvShow(TvShowSample.Grimm)
            }
        }

        runComposeAppTest {
            homeRobot
                .openDrawer()
                .openMyLists()
                .openLiked()
                .openTvShow(TvShowSample.Grimm.title)
                .verify { tvShowDetailsIsDisplayed() }
        }
    }

    @Test
    fun givenRatedListIsDisplayed_whenMovieIsSelected_detailsIsDisplayed() {
        appRule {
            rated {
                movie(MovieSample.Inception, Rating.of(9))
            }
        }

        runComposeAppTest {
            homeRobot
                .openDrawer()
                .openMyLists()
                .openRated()
                .openMovie(MovieSample.Inception.title)
                .verify { movieDetailsIsDisplayed() }
        }
    }

    @Test
    fun givenRatedListIsDisplayed_whenTvShowIsSelected_detailsIsDisplayed() {
        appRule {
            rated {
                tvShow(TvShowSample.Grimm, Rating.of(8))
            }
        }

        runComposeAppTest {
            homeRobot
                .openDrawer()
                .openMyLists()
                .openRated()
                .openTvShow(TvShowSample.Grimm.title)
                .verify { tvShowDetailsIsDisplayed() }
        }
    }

    @Test
    fun givenWatchlistIsDisplayed_whenMovieIsSelected_detailsIsDisplayed() {
        appRule {
            watchlist {
                movie(MovieSample.Inception)
            }
        }

        runComposeAppTest {
            homeRobot
                .openDrawer()
                .openWatchlist()
                .openMovie(MovieSample.Inception.title)
                .verify { movieDetailsIsDisplayed() }
        }
    }

    @Test
    fun givenWatchlistIsDisplayed_whenTvShowIsSelected_detailsIsDisplayed() {
        appRule {
            watchlist {
                tvShow(TvShowSample.Grimm)
            }
        }

        runComposeAppTest {
            homeRobot
                .openDrawer()
                .openWatchlist()
                .openTvShow(TvShowSample.Grimm.title)
                .verify { tvShowDetailsIsDisplayed() }
        }
    }
}
