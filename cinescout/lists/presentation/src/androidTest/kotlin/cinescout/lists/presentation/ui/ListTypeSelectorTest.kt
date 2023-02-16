package cinescout.lists.presentation.ui

import cinescout.lists.presentation.model.ListType
import cinescout.test.compose.robot.ListRobot
import cinescout.test.compose.runComposeTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ListTypeSelectorTest {

    @BeforeTest
    fun setup() {
        ListTypeSelector.animateChanges = false
    }

    @Test
    fun whenTypeIsAll_thenAllIsSelected() = runComposeTest {
        ListRobot { ListTypeSelector(type = ListType.All, onTypeSelected = {}) }
            .verify { allTypeIsSelected() }
    }

    @Test
    fun whenAllIsSelected_thenCallbackIsCalled() = runComposeTest {
        var typeSelected: ListType? = null
        ListRobot { ListTypeSelector(type = ListType.Movies, onTypeSelected = { typeSelected = it }) }
            .selectAllType()
        assertEquals(ListType.All, typeSelected)
    }

    @Test
    fun givenTypeIsMovies_whenAllIsSelected_thenAllIsSelected() = runComposeTest {
        ListRobot { ListTypeSelector(type = ListType.Movies, onTypeSelected = {}) }
            .selectAllType()
            .verify { allTypeIsSelected() }
    }

    @Test
    fun whenTypeIsMovies_thenMoviesIsSelected() = runComposeTest {
        ListRobot { ListTypeSelector(type = ListType.Movies, onTypeSelected = {}) }
            .verify { moviesTypeIsSelected() }
    }

    @Test
    fun whenMoviesIsSelected_thenCallbackIsCalled() = runComposeTest {
        var typeSelected: ListType? = null
        ListRobot { ListTypeSelector(type = ListType.All, onTypeSelected = { typeSelected = it }) }
            .selectMoviesType()
        assertEquals(ListType.Movies, typeSelected)
    }

    @Test
    fun givenTypeIsAll_whenMoviesIsSelected_thenMoviesIsSelected() = runComposeTest {
        ListRobot { ListTypeSelector(type = ListType.All, onTypeSelected = {}) }
            .selectMoviesType()
            .verify { moviesTypeIsSelected() }
    }

    @Test
    fun whenTypeIsTvShows_thenTvShowsIsSelected() = runComposeTest {
        ListRobot { ListTypeSelector(type = ListType.TvShows, onTypeSelected = {}) }
            .verify { tvShowsTypeIsSelected() }
    }

    @Test
    fun whenTvShowsIsSelected_thenCallbackIsCalled() = runComposeTest {
        var typeSelected: ListType? = null
        ListRobot { ListTypeSelector(type = ListType.All, onTypeSelected = { typeSelected = it }) }
            .selectTvShowsType()
        assertEquals(ListType.TvShows, typeSelected)
    }

    @Test
    fun givenTypeIsAll_whenTvShowsIsSelected_thenTvShowsIsSelected() = runComposeTest {
        ListRobot { ListTypeSelector(type = ListType.All, onTypeSelected = {}) }
            .selectTvShowsType()
            .verify { tvShowsTypeIsSelected() }
    }
}
