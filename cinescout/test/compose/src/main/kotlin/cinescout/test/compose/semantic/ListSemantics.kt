package cinescout.test.compose.semantic

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.onNodeWithTag
import cinescout.design.R.string
import cinescout.design.TestTag
import cinescout.test.compose.util.hasText

context(ComposeUiTest)
class ListSemantics {

    fun dislikedScreen() = onNodeWithTag(TestTag.Disliked)
    fun dislikedSubtitle() = onNode(hasText(string.lists_disliked) and isSelectable().not())
    fun likedScreen() = onNodeWithTag(TestTag.Liked)
    fun likedSubtitle() = onNode(hasText(string.lists_liked) and isSelectable().not())
    fun ratedScreen() = onNodeWithTag(TestTag.Rated)
    fun ratedSubtitle() = onNode(hasText(string.lists_rated) and isSelectable().not())
    fun watchlistScreen() = onNodeWithTag(TestTag.Watchlist)
    fun watchlistSubtitle() = onNode(hasText(string.lists_watchlist) and isSelectable().not())
}

context(ComposeUiTest)
fun <T> ListSemantics(block: context(ListSemantics) () -> T): T =
    with(ListSemantics(), block)
