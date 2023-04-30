package cinescout.lists.presentation.state

import cinescout.lists.domain.ListSorting
import cinescout.lists.presentation.model.ListFilter
import cinescout.lists.presentation.model.ListItemUiModel
import cinescout.resources.R
import cinescout.resources.TextRes
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.utils.compose.Effect
import cinescout.utils.compose.paging.PagingItemsState

data class ItemsListState(
    val filter: ListFilter,
    val itemsState: PagingItemsState<ListItemUiModel>,
    val scrollToTop: Effect<Unit>,
    val sorting: ListSorting,
    val type: ScreenplayTypeFilter
) {

    val errorMessage: Effect<TextRes>
        get() = itemsState.errorMessage
    
    val emptyMessage: TextRes
        get() = when (type) {
            ScreenplayTypeFilter.All -> when (filter) {
                ListFilter.Disliked -> R.string.lists_disliked_all_empty
                ListFilter.Liked -> R.string.lists_liked_all_empty
                ListFilter.Rated -> R.string.lists_rated_all_empty
                ListFilter.Watchlist -> R.string.lists_watchlist_all_empty
            }
            ScreenplayTypeFilter.Movies -> when (filter) {
                ListFilter.Disliked -> R.string.lists_disliked_movies_empty
                ListFilter.Liked -> R.string.lists_liked_movies_empty
                ListFilter.Rated -> R.string.lists_rated_movies_empty
                ListFilter.Watchlist -> R.string.lists_watchlist_movies_empty
            }
            ScreenplayTypeFilter.TvShows -> when (filter) {
                ListFilter.Disliked -> R.string.lists_disliked_tv_shows_empty
                ListFilter.Liked -> R.string.lists_liked_tv_shows_empty
                ListFilter.Rated -> R.string.lists_rated_tv_shows_empty
                ListFilter.Watchlist -> R.string.lists_watchlist_tv_shows_empty
            }
        }.let(TextRes::invoke)
}
