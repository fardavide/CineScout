package cinescout.lists.presentation.state

import arrow.core.Option
import cinescout.lists.domain.ListSorting
import cinescout.lists.presentation.model.ListFilter
import cinescout.lists.presentation.model.ListItemUiModel
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.screenplay.domain.model.Genre
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.utils.compose.Effect
import cinescout.utils.compose.paging.PagingItemsState
import kotlinx.collections.immutable.ImmutableList

data class ItemsListState(
    val availableGenres: ImmutableList<Genre>,
    val genreFilter: Option<Genre>,
    val listFilter: ListFilter,
    val itemsState: PagingItemsState<ListItemUiModel>,
    val scrollToTop: Effect<Unit>,
    val sorting: ListSorting,
    val type: ScreenplayTypeFilter
) {

    val errorMessage: Effect<TextRes>
        get() = itemsState.errorMessage

    // TODO: unify strings?
    val emptyMessage: TextRes
        get() = when (type) {
            ScreenplayTypeFilter.All -> when (listFilter) {
                ListFilter.Disliked -> string.lists_disliked_all_empty
                ListFilter.InProgress -> string.lists_in_progress_all_empty
                ListFilter.Liked -> string.lists_liked_all_empty
                ListFilter.Rated -> string.lists_rated_all_empty
                ListFilter.Watchlist -> string.lists_watchlist_all_empty
            }
            ScreenplayTypeFilter.Movies -> when (listFilter) {
                ListFilter.Disliked -> string.lists_disliked_movies_empty
                ListFilter.InProgress -> string.lists_in_progress_movies_empty
                ListFilter.Liked -> string.lists_liked_movies_empty
                ListFilter.Rated -> string.lists_rated_movies_empty
                ListFilter.Watchlist -> string.lists_watchlist_movies_empty
            }
            ScreenplayTypeFilter.TvShows -> when (listFilter) {
                ListFilter.Disliked -> string.lists_disliked_tv_shows_empty
                ListFilter.InProgress -> string.lists_in_progress_tv_shows_empty
                ListFilter.Liked -> string.lists_liked_tv_shows_empty
                ListFilter.Rated -> string.lists_rated_tv_shows_empty
                ListFilter.Watchlist -> string.lists_watchlist_tv_shows_empty
            }
        }.let(TextRes::invoke)
}
