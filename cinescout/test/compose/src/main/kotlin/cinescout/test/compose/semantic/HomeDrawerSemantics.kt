package cinescout.test.compose.semantic

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import cinescout.design.TestTag
import cinescout.resources.R.string
import cinescout.test.compose.util.hasText

context(ComposeUiTest)
class HomeDrawerSemantics {

    fun accounts() = onAllNodes(hasText(string.home_login) or hasText(string.home_manage_accounts)).onFirst()
    fun drawer() = onNodeWithTag(TestTag.Drawer)
    fun forYou() = onNode(hasText(string.suggestions_for_you) and isSelectable())
    fun myLists() = onNode(hasText(string.lists_my_lists) and isSelectable())
    fun watchlist() = onNode(hasText(string.lists_watchlist) and isSelectable())
}

context(ComposeUiTest)
fun <T> HomeDrawerSemantics(block: context(HomeDrawerSemantics) () -> T): T =
    with(HomeDrawerSemantics(), block)
