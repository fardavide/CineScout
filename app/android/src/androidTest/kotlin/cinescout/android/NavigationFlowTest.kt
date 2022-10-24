package cinescout.android

import cinescout.android.testutil.PostNotificationsRule
import cinescout.android.testutil.homeRobot
import cinescout.android.testutil.runComposeAppTest
import cinescout.common.model.Rating
import cinescout.lists.presentation.ui.ListTypeSelector
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.suggestions.presentation.ui.ForYouTypeSelector
import cinescout.test.compose.robot.HomeRobot.Companion.verify
import cinescout.test.mock.MockAppRule
import cinescout.tvshows.domain.testdata.TvShowTestData
import org.junit.Rule
import kotlin.test.BeforeTest
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
            .selectForYou()
            .verify { forYouIsDisplayed() }
    }

    @Test
    fun givenHomeIsDisplayed_whenForYouIsOpen_subtitleIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .selectForYou()
            .verify { forYouSubtitleIsDisplayed() }
    }

    @Test
    fun givenHomeIsDisplayed_whenMyListsIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .selectMyLists()
            .verify { myListsIsDisplayed() }
    }

    @Test
    fun givenHomeIsDisplayed_whenMyListsIsOpen_subtitleIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .selectMyLists()
            .verify { myListSubtitleIsDisplayed() }
    }

    @Test
    fun givenHomeIsDisplayed_whenWatchlistIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .selectWatchlist()
            .verify { watchlistIsDisplayed() }
    }

    @Test
    fun givenHomeIsDisplayed_whenWatchlistIsOpen_subtitleIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .selectWatchlist()
            .verify { watchlistSubtitleIsDisplayed() }
    }

    @Test
    fun givenForYouIsDisplayed_whenMovieIsSelected_detailsIsDisplayed() {
        appRule {
            forYou {
                movie(MovieTestData.Inception)
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
    fun givenForYouIsDisplayed_whenTvShowIsSelected_detailsIsDisplayed() {
        appRule {
            forYou {
                tvShow(TvShowTestData.Grimm)
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
            .selectDisliked()
            .verify { dislikedIsDisplayed() }
    }

    @Test
    fun givenMyListsIsDisplayed_whenDislikedIsOpen_subtitleIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openMyLists()
            .selectDisliked()
            .verify { dislikedSubtitleIsDisplayed() }
    }

    @Test
    fun givenMyListsIsDisplayed_whenLikedIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openMyLists()
            .selectLiked()
            .verify { likedIsDisplayed() }
    }

    @Test
    fun givenMyListsIsDisplayed_whenLikedIsOpen_subtitleIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openMyLists()
            .selectLiked()
            .verify { likedSubtitleIsDisplayed() }
    }

    @Test
    fun givenMyListsIsDisplayed_whenRatedIsSelected_screenIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openMyLists()
            .selectRated()
            .verify { ratedIsDisplayed() }
    }

    @Test
    fun givenMyListsIsDisplayed_whenRatedIsOpen_subtitleIsDisplayed() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openMyLists()
            .selectRated()
            .verify { ratedSubtitleIsDisplayed() }
    }

    @Test
    fun givenDislikedListIsDisplayed_whenMovieIsSelected_detailsIsDisplayed() {
        appRule {
            disliked {
                movie(MovieTestData.War)
            }
        }

        runComposeAppTest {
            homeRobot
                .openDrawer()
                .openMyLists()
                .openDisliked()
                .openMovie(MovieTestData.War.title)
                .verify { movieDetailsIsDisplayed() }
        }
    }

    @Test
    fun givenDislikedListIsDisplayed_whenTvShowIsSelected_detailsIsDisplayed() {
        appRule {
            disliked {
                tvShow(TvShowTestData.BreakingBad)
            }
        }

        runComposeAppTest {
            homeRobot
                .openDrawer()
                .openMyLists()
                .openDisliked()
                .openTvShow(TvShowTestData.BreakingBad.title)
                .verify { tvShowDetailsIsDisplayed() }
        }
    }

    @Test
    fun givenLikedListIsDisplayed_whenMovieIsSelected_detailsIsDisplayed() {
        appRule {
            liked {
                movie(MovieTestData.Inception)
            }
        }

        runComposeAppTest {
            homeRobot
                .openDrawer()
                .openMyLists()
                .openLiked()
                .openMovie(MovieTestData.Inception.title)
                .verify { movieDetailsIsDisplayed() }
        }
    }

    @Test
    fun givenLikedListIsDisplayed_whenTvShowIsSelected_detailsIsDisplayed() {
        appRule {
            liked {
                tvShow(TvShowTestData.Grimm)
            }
        }

        runComposeAppTest {
            homeRobot
                .openDrawer()
                .openMyLists()
                .openLiked()
                .openTvShow(TvShowTestData.Grimm.title)
                .verify { tvShowDetailsIsDisplayed() }
        }
    }

    @Test
    fun givenRatedListIsDisplayed_whenMovieIsSelected_detailsIsDisplayed() {
        appRule {
            rated {
                movie(MovieTestData.Inception, Rating.of(9))
            }
        }

        runComposeAppTest {
            homeRobot
                .openDrawer()
                .openMyLists()
                .openRated()
                .openMovie(MovieTestData.Inception.title)
                .verify { movieDetailsIsDisplayed() }
        }
    }

    @Test
    fun givenRatedListIsDisplayed_whenTvShowIsSelected_detailsIsDisplayed() {
        appRule {
            rated {
                tvShow(TvShowTestData.Grimm, Rating.of(8))
            }
        }

        runComposeAppTest {
            homeRobot
                .openDrawer()
                .openMyLists()
                .openRated()
                .openTvShow(TvShowTestData.Grimm.title)
                .verify { tvShowDetailsIsDisplayed() }
        }
    }

    @Test
    fun givenWatchlistIsDisplayed_whenMovieIsSelected_detailsIsDisplayed() {
        appRule {
            watchlist {
                movie(MovieTestData.Inception)
            }
        }

        runComposeAppTest {
            homeRobot
                .openDrawer()
                .openWatchlist()
                .openMovie(MovieTestData.Inception.title)
                .verify { movieDetailsIsDisplayed() }
        }
    }

    @Test
    fun givenWatchlistIsDisplayed_whenTvShowIsSelected_detailsIsDisplayed() {
        appRule {
            watchlist {
                tvShow(TvShowTestData.Grimm)
            }
        }

        runComposeAppTest {
            homeRobot
                .openDrawer()
                .openWatchlist()
                .openTvShow(TvShowTestData.Grimm.title)
                .verify { tvShowDetailsIsDisplayed() }
        }
    }
}
