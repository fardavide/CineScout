package cinescout.test.compose.semantic

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import cinescout.design.R.string
import cinescout.design.TestTag
import cinescout.test.compose.util.hasText
import cinescout.test.compose.util.onNodeWithText

context(ComposeUiTest)
class ListSemantics {

    fun allType() = onNodeWithText(string.item_type_all)
    fun dislikedScreen() = onNodeWithTag(TestTag.Disliked)
    fun dislikedSubtitle() = onNode(hasText(string.lists_disliked) and isSelectable().not())
    fun emptyAllRated() = onNodeWithText(string.lists_rated_all_empty)
    fun emptyAllWatchlist() = onNodeWithText(string.lists_watchlist_all_empty)
    fun emptyMoviesRated() = onNodeWithText(string.lists_rated_movies_empty)
    fun emptyMoviesWatchlist() = onNodeWithText(string.lists_watchlist_movies_empty)
    fun emptyTvShowsRated() = onNodeWithText(string.lists_rated_tv_shows_empty)
    fun emptyTvShowsWatchlist() = onNodeWithText(string.lists_watchlist_tv_shows_empty)
    fun likedScreen() = onNodeWithTag(TestTag.Liked)
    fun likedSubtitle() = onNode(hasText(string.lists_liked) and isSelectable().not())
    fun moviesType() = onNodeWithText(string.item_type_movies)
    fun ratedScreen() = onNodeWithTag(TestTag.Rated)
    fun ratedSubtitle() = onNode(hasText(string.lists_rated) and isSelectable().not())
    fun title(title: String) = onNodeWithText(title)
    fun tvShowsType() = onNodeWithText(string.item_type_tv_shows)
    fun watchlistScreen() = onNodeWithTag(TestTag.Watchlist)
    fun watchlistSubtitle() = onNode(hasText(string.lists_watchlist) and isSelectable().not())
}

context(ComposeUiTest)
fun <T> ListSemantics(block: context(ListSemantics) () -> T): T =
    with(ListSemantics(), block)
