package cinescout.lists.presentation.previewdata

import arrow.core.none
import cinescout.design.util.PreviewDataProvider
import cinescout.lists.domain.ListSorting
import cinescout.lists.presentation.model.ListFilter
import cinescout.lists.presentation.sample.ListItemUiModelSample
import cinescout.lists.presentation.state.ItemsListState
import cinescout.resources.sample.MessageSample
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.utils.compose.Effect
import cinescout.utils.compose.paging.PagingItemsState
import cinescout.utils.compose.paging.unsafeLazyPagingItemsOf
import kotlinx.collections.immutable.persistentListOf

object ItemsListScreenPreviewData {

    val AllEmptyList = ItemsListState(
        availableGenres = persistentListOf(),
        genreFilter = none(),
        itemsState = PagingItemsState.Empty,
        listFilter = ListFilter.Disliked,
        scrollToTop = Effect.empty(),
        sorting = ListSorting.Rating.Descending,
        type = ScreenplayTypeFilter.All
    )
    val Error = ItemsListState(
        availableGenres = persistentListOf(),
        genreFilter = none(),
        itemsState = PagingItemsState.Error(MessageSample.NoNetworkError),
        listFilter = ListFilter.Disliked,
        scrollToTop = Effect.empty(),
        sorting = ListSorting.Rating.Descending,
        type = ScreenplayTypeFilter.All
    )
    val Loading = ItemsListState(
        availableGenres = persistentListOf(),
        genreFilter = none(),
        itemsState = PagingItemsState.Loading,
        listFilter = ListFilter.Disliked,
        scrollToTop = Effect.empty(),
        sorting = ListSorting.Rating.Descending,
        type = ScreenplayTypeFilter.All
    )
    val MoviesEmptyList = ItemsListState(
        availableGenres = persistentListOf(),
        genreFilter = none(),
        itemsState = PagingItemsState.Empty,
        listFilter = ListFilter.Disliked,
        scrollToTop = Effect.empty(),
        sorting = ListSorting.Rating.Descending,
        type = ScreenplayTypeFilter.Movies
    )
    val NotEmptyList = ItemsListState(
        availableGenres = persistentListOf(),
        genreFilter = none(),
        itemsState = PagingItemsState.NotEmpty(
            unsafeLazyPagingItemsOf(
                ListItemUiModelSample.Inception,
                ListItemUiModelSample.TheWolfOfWallStreet
            ),
            error = Effect.empty(),
            isAlsoLoading = false
        ),
        listFilter = ListFilter.Disliked,
        scrollToTop = Effect.empty(),
        sorting = ListSorting.Rating.Descending,
        type = ScreenplayTypeFilter.All
    )
    val TvShowsEmptyWatchlist = ItemsListState(
        availableGenres = persistentListOf(),
        genreFilter = none(),
        itemsState = PagingItemsState.Empty,
        listFilter = ListFilter.Disliked,
        scrollToTop = Effect.empty(),
        sorting = ListSorting.Rating.Descending,
        type = ScreenplayTypeFilter.TvShows
    )
}

internal class ItemsListScreenPreviewDataProvider : PreviewDataProvider<ItemsListState>(
    ItemsListScreenPreviewData.Loading,
    ItemsListScreenPreviewData.Error,
    ItemsListScreenPreviewData.AllEmptyList,
    ItemsListScreenPreviewData.MoviesEmptyList,
    ItemsListScreenPreviewData.TvShowsEmptyWatchlist,
    ItemsListScreenPreviewData.NotEmptyList
)
