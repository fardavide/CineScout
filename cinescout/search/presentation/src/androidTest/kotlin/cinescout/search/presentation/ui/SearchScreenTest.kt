package cinescout.search.presentation.ui

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.runComposeUiTest
import cinescout.search.presentation.state.SearchState
import cinescout.test.compose.robot.SearchRobot
import cinescout.utils.compose.paging.PagingItemsState
import kotlin.test.Test

class SearchScreenTest {

    @Test
    fun whenQueryIsInception_inceptionIsDisplayed() = runComposeUiTest {
        setupScreen(query = "Inception")
            .verify { searchQueryIsDisplayed("Inception") }
    }

    @Test
    fun givenQueryIsInception_whenChangedToDexter_dexterIsDisplayed() = runComposeUiTest {
        setupScreen(query = "Inception")
            .search("Dexter")
            .verify { searchQueryIsDisplayed("Dexter") }
    }

    context(ComposeUiTest)
    private fun setupScreen(query: String): SearchRobot {
        val state = SearchState(query = query, itemsState = PagingItemsState.Loading)
        return SearchRobot { SearchScreen(state = state, search = {}, openItem = {}) }
    }
}
