package cinescout.test.compose.robot

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performTextInput
import cinescout.test.compose.semantic.HomeSemantics
import cinescout.test.compose.semantic.SearchSemantics
import cinescout.test.compose.util.awaitDisplayed

context(ComposeUiTest, SearchSemantics)
class SearchRobot internal constructor() {

    fun awaitIdle(): SearchRobot {
        waitForIdle()
        return this
    }

    fun awaitScreenplay(title: String): SearchRobot {
        title(title).awaitDisplayed()
        return this
    }

    fun search(query: String): SearchRobot {
        searchField().performTextInput(query)
        return this
    }

    fun verify(block: Verify.() -> Unit): SearchRobot {
        HomeSemantics { block(Verify()) }
        return this
    }

    context(ComposeUiTest, SearchSemantics, HomeSemantics)
    class Verify internal constructor() : HomeRobot.Verify() {

        fun searchQueryIsDisplayed(query: String) {
            searchQuery(query).assertIsDisplayed()
        }

        fun screenIsDisplayed() {
            screen().assertIsDisplayed()
        }

        fun titleIsDisplayed() {
            title().assertIsDisplayed()
        }
    }
}

context(ComposeUiTest)
fun SearchRobot(content: @Composable () -> Unit): SearchRobot {
    setContent(content)
    return SearchSemantics { SearchRobot() }
}
