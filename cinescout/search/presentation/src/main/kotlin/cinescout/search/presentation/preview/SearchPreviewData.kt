package cinescout.search.presentation.preview

import cinescout.design.util.PreviewDataProvider
import cinescout.resources.sample.MessageSample
import cinescout.search.presentation.sample.SearchItemUiModelSample
import cinescout.search.presentation.state.SearchState
import cinescout.utils.compose.Effect
import cinescout.utils.compose.paging.PagingItemsState
import cinescout.utils.compose.paging.unsafeLazyPagingItemsOf

internal object SearchPreviewData {

    val QueryMovies_E = SearchState(
        query = "e",
        itemsState = PagingItemsState.NotEmpty(
            items = unsafeLazyPagingItemsOf(
                SearchItemUiModelSample.BreakingBad,
                SearchItemUiModelSample.Dexter,
                SearchItemUiModelSample.Inception,
                SearchItemUiModelSample.TheWolfOfWallStreet
            ),
            error = Effect.empty(),
            isAlsoLoading = false
        )
    )

    val QueryMovies_Inc = SearchState(
        query = "inc",
        itemsState = PagingItemsState.NotEmpty(
            items = unsafeLazyPagingItemsOf(
                SearchItemUiModelSample.Inception
            ),
            error = Effect.empty(),
            isAlsoLoading = false
        )
    )

    val QueryTvShows_Gri = SearchState(
        query = "gri",
        itemsState = PagingItemsState.NotEmpty(
            items = unsafeLazyPagingItemsOf(
                SearchItemUiModelSample.Grimm
            ),
            error = Effect.empty(),
            isAlsoLoading = false
        )
    )

    val QueryTvShows_R = SearchState(
        query = "r",
        itemsState = PagingItemsState.NotEmpty(
            items = unsafeLazyPagingItemsOf(
                SearchItemUiModelSample.BreakingBad,
                SearchItemUiModelSample.Dexter,
                SearchItemUiModelSample.Grimm,
                SearchItemUiModelSample.TheWolfOfWallStreet
            ),
            error = Effect.empty(),
            isAlsoLoading = false
        )
    )

    val Idle = SearchState(
        query = "",
        itemsState = PagingItemsState.Empty
    )

    val NoResults = SearchState(
        query = "something",
        itemsState = PagingItemsState.Empty
    )

    val InitialLoading = SearchState(
        query = "something",
        itemsState = PagingItemsState.Loading
    )

    val NoNetwork = SearchState(
        query = "inception",
        itemsState = PagingItemsState.Error(
            message = MessageSample.NoNetworkError
        )
    )
}

internal class SearchPreviewDataProvider : PreviewDataProvider<SearchState>(
    SearchPreviewData.QueryMovies_E,
    SearchPreviewData.QueryMovies_Inc,
    SearchPreviewData.QueryTvShows_R,
    SearchPreviewData.QueryTvShows_Gri,
    SearchPreviewData.Idle,
    SearchPreviewData.NoResults,
    SearchPreviewData.InitialLoading,
    SearchPreviewData.NoNetwork
)
