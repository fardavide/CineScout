package cinescout.search.presentation.preview

import cinescout.CineScoutTestApi
import cinescout.design.util.PreviewDataProvider
import cinescout.resources.sample.MessageSample
import cinescout.search.presentation.sample.SearchItemUiModelSample
import cinescout.search.presentation.state.SearchState
import cinescout.utils.compose.Effect
import cinescout.utils.compose.paging.PagingItemsState
import cinescout.utils.compose.paging.lazyPagingItemsOf

@CineScoutTestApi
internal object SearchPreviewData {

    val QueryMovies_E = SearchState(
        query = "e",
        itemsState = PagingItemsState.NotEmpty(
            items = lazyPagingItemsOf(
                SearchItemUiModelSample.BreakingBad,
                SearchItemUiModelSample.Dexter,
                SearchItemUiModelSample.Inception,
                SearchItemUiModelSample.TheWolfOfWallStreet
            ),
            error = Effect.empty(),
            isAlsoLoading = false
        ),
        searchFieldIcon = SearchState.SearchFieldIcon.Clear
    )

    val QueryMovies_Inc = SearchState(
        query = "inc",
        itemsState = PagingItemsState.NotEmpty(
            items = lazyPagingItemsOf(
                SearchItemUiModelSample.Inception
            ),
            error = Effect.empty(),
            isAlsoLoading = false
        ),
        searchFieldIcon = SearchState.SearchFieldIcon.Clear
    )

    val QueryTvShows_Gri = SearchState(
        query = "gri",
        itemsState = PagingItemsState.NotEmpty(
            items = lazyPagingItemsOf(
                SearchItemUiModelSample.Grimm
            ),
            error = Effect.empty(),
            isAlsoLoading = false
        ),
        searchFieldIcon = SearchState.SearchFieldIcon.Clear
    )

    val QueryTvShows_R = SearchState(
        query = "r",
        itemsState = PagingItemsState.NotEmpty(
            items = lazyPagingItemsOf(
                SearchItemUiModelSample.BreakingBad,
                SearchItemUiModelSample.Dexter,
                SearchItemUiModelSample.Grimm,
                SearchItemUiModelSample.TheWolfOfWallStreet
            ),
            error = Effect.empty(),
            isAlsoLoading = false
        ),
        searchFieldIcon = SearchState.SearchFieldIcon.Clear
    )

    val Idle = SearchState(
        query = "",
        itemsState = PagingItemsState.Empty,
        searchFieldIcon = SearchState.SearchFieldIcon.None
    )

    val NoResults = SearchState(
        query = "something",
        itemsState = PagingItemsState.Empty,
        searchFieldIcon = SearchState.SearchFieldIcon.Clear
    )

    val InitialLoading = SearchState(
        query = "something",
        itemsState = PagingItemsState.Loading,
        searchFieldIcon = SearchState.SearchFieldIcon.Loading
    )

    val NoNetwork = SearchState(
        query = "inception",
        itemsState = PagingItemsState.Error(
            message = MessageSample.NoNetworkError
        ),
        searchFieldIcon = SearchState.SearchFieldIcon.Clear
    )
}

@CineScoutTestApi
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
