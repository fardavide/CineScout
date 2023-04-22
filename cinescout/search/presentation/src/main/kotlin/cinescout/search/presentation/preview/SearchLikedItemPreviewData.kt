package cinescout.search.presentation.preview

import cinescout.design.util.PreviewDataProvider
import cinescout.resources.sample.MessageSample
import cinescout.search.presentation.sample.SearchLikedItemUiModelSample
import cinescout.search.presentation.state.SearchLikedItemState
import cinescout.utils.compose.Effect
import cinescout.utils.compose.paging.PagingItemsState
import cinescout.utils.compose.paging.unsafeLazyPagingItemsOf

object SearchLikedItemPreviewData {

    val QueryMovies_E = SearchLikedItemState(
        query = "e",
        itemsState = PagingItemsState.NotEmpty(
            items = unsafeLazyPagingItemsOf(
                SearchLikedItemUiModelSample.Inception,
                SearchLikedItemUiModelSample.TheWolfOfWallStreet
            ),
            error = Effect.empty(),
            isAlsoLoading = false
        )
    )

    val QueryMovies_Inc = SearchLikedItemState(
        query = "inc",
        itemsState = PagingItemsState.NotEmpty(
            items = unsafeLazyPagingItemsOf(
                SearchLikedItemUiModelSample.Inception
            ),
            error = Effect.empty(),
            isAlsoLoading = false
        )
    )

    val QueryTvShows_Gri = SearchLikedItemState(
        query = "gri",
        itemsState = PagingItemsState.NotEmpty(
            items = unsafeLazyPagingItemsOf(
                SearchLikedItemUiModelSample.Grimm
            ),
            error = Effect.empty(),
            isAlsoLoading = false
        )
    )

    val QueryTvShows_R = SearchLikedItemState(
        query = "r",
        itemsState = PagingItemsState.NotEmpty(
            items = unsafeLazyPagingItemsOf(
                SearchLikedItemUiModelSample.Dexter,
                SearchLikedItemUiModelSample.Grimm
            ),
            error = Effect.empty(),
            isAlsoLoading = false
        )
    )

    val Idle = SearchLikedItemState(
        query = "",
        itemsState = PagingItemsState.Empty
    )

    val NoResults = SearchLikedItemState(
        query = "something",
        itemsState = PagingItemsState.Empty
    )

    val InitialLoading = SearchLikedItemState(
        query = "something",
        itemsState = PagingItemsState.Loading
    )

    val NoNetwork = SearchLikedItemState(
        query = "inception",
        itemsState = PagingItemsState.Error(
            message = MessageSample.NoNetworkError
        )
    )
}

class SearchLikedItemPreviewDataProvider : PreviewDataProvider<SearchLikedItemState>(
    SearchLikedItemPreviewData.QueryMovies_E,
    SearchLikedItemPreviewData.QueryMovies_Inc,
    SearchLikedItemPreviewData.QueryTvShows_R,
    SearchLikedItemPreviewData.QueryTvShows_Gri,
    SearchLikedItemPreviewData.Idle,
    SearchLikedItemPreviewData.NoResults,
    SearchLikedItemPreviewData.InitialLoading,
    SearchLikedItemPreviewData.NoNetwork
)
