package cinescout.search.presentation.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import arrow.core.nonEmptyListOf
import arrow.core.none
import cinescout.design.testdata.MessageSample
import cinescout.search.presentation.model.SearchLikedItemState
import cinescout.search.presentation.sample.SearchLikedItemUiModelSample

object SearchLikedItemPreviewData {

    val QueryMovies_E = SearchLikedItemState(
        query = "e",
        result = SearchLikedItemState.SearchResult.Data(
            items = nonEmptyListOf(
                SearchLikedItemUiModelSample.Inception,
                SearchLikedItemUiModelSample.TheWolfOfWallStreet
            )
        )
    )

    val QueryMovies_Inc = SearchLikedItemState(
        query = "inc",
        result = SearchLikedItemState.SearchResult.Data(
            items = nonEmptyListOf(
                SearchLikedItemUiModelSample.Inception
            )
        )
    )

    val QueryTvShows_Gri = SearchLikedItemState(
        query = "gri",
        result = SearchLikedItemState.SearchResult.Data(
            items = nonEmptyListOf(
                SearchLikedItemUiModelSample.Grimm
            )
        )
    )

    val QueryTvShows_R = SearchLikedItemState(
        query = "r",
        result = SearchLikedItemState.SearchResult.Data(
            items = nonEmptyListOf(
                SearchLikedItemUiModelSample.Dexter,
                SearchLikedItemUiModelSample.Grimm
            )
        )
    )

    val Idle = SearchLikedItemState(
        query = "",
        result = SearchLikedItemState.SearchResult.Idle
    )

    val NoResults = SearchLikedItemState(
        query = "something",
        result = SearchLikedItemState.SearchResult.NoResults
    )

    val InitialLoading = SearchLikedItemState(
        query = "something",
        result = SearchLikedItemState.SearchResult.Loading(previousItems = none())
    )

    val NoNetwork = SearchLikedItemState(
        query = "inception",
        result = SearchLikedItemState.SearchResult.Error(message = MessageSample.NoNetworkError)
    )
}

class SearchLikedItemPreviewDataProvider : PreviewParameterProvider<SearchLikedItemState> {

    override val values = sequenceOf(
        SearchLikedItemPreviewData.QueryMovies_E,
        SearchLikedItemPreviewData.QueryMovies_Inc,
        SearchLikedItemPreviewData.QueryTvShows_R,
        SearchLikedItemPreviewData.QueryTvShows_Gri,
        SearchLikedItemPreviewData.Idle,
        SearchLikedItemPreviewData.NoResults,
        SearchLikedItemPreviewData.InitialLoading,
        SearchLikedItemPreviewData.NoNetwork
    )
}
