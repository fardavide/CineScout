package cinescout.suggestions.presentation.ui

import cinescout.suggestions.presentation.model.ForYouType
import cinescout.test.compose.robot.ForYouRobot
import cinescout.test.compose.runComposeTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ForYouTypeSelectorTest {

    @BeforeTest
    fun setup() {
        ForYouTypeSelector.animateChanges = false
    }

    @Test
    fun whenTypeIsMovies_thenMoviesIsSelected() = runComposeTest {
        ForYouRobot { ForYouTypeSelector(type = ForYouType.Movies, onTypeSelected = {}) }
            .verify { moviesTypeIsSelected() }
    }

    @Test
    fun givenTypeIsMovies_whenTvShowsIsSelected_thenTvShowsIsSelected() = runComposeTest {
        ForYouRobot { ForYouTypeSelector(type = ForYouType.Movies, onTypeSelected = {}) }
            .selectTvShowsType()
            .verify { tvShowsTypeIsSelected() }
    }

    @Test
    fun whenMoviesIsSelected_thenCallbackIsCalled() = runComposeTest {
        var typeSelected: ForYouType? = null
        ForYouRobot { ForYouTypeSelector(type = ForYouType.TvShows, onTypeSelected = { typeSelected = it }) }
            .selectMoviesType()
        assertEquals(ForYouType.Movies, typeSelected)
    }

    @Test
    fun whenTypeIsTvShows_thenTvShowsIsSelected() = runComposeTest {
        ForYouRobot { ForYouTypeSelector(type = ForYouType.TvShows, onTypeSelected = {}) }
            .verify { tvShowsTypeIsSelected() }
    }

    @Test
    fun givenTypeIsTvShows_whenTvMoviesIsSelected_thenTvMoviesIsSelected() = runComposeTest {
        ForYouRobot { ForYouTypeSelector(type = ForYouType.TvShows, onTypeSelected = {}) }
            .selectMoviesType()
            .verify { moviesTypeIsSelected() }
    }

    @Test
    fun whenTvShowsIsSelected_thenCallbackIsCalled() = runComposeTest {
        var typeSelected: ForYouType? = null
        ForYouRobot { ForYouTypeSelector(type = ForYouType.Movies, onTypeSelected = { typeSelected = it }) }
            .selectTvShowsType()
        assertEquals(ForYouType.TvShows, typeSelected)
    }
}
