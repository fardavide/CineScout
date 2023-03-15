package cinescout.search.presentation.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import cinescout.search.presentation.model.SearchLikedItemState

object SearchLikedItemPreviewData {

    val QueryMovies_E = SearchLikedItemState(
        query = "e",
        items = TODO()
//            items = nonEmptyListOf(
//                SearchLikedItemUiModelSample.Inception,
//                SearchLikedItemUiModelSample.TheWolfOfWallStreet
//            )
    )

    val QueryMovies_Inc = SearchLikedItemState(
        query = "inc",
        items = TODO()
//            items = nonEmptyListOf(
//                SearchLikedItemUiModelSample.Inception
//            )
    )

    val QueryTvShows_Gri = SearchLikedItemState(
        query = "gri",
        items = TODO()
//            items = nonEmptyListOf(
//                SearchLikedItemUiModelSample.Grimm
//            )
    )

    val QueryTvShows_R = SearchLikedItemState(
        query = "r",
        items = TODO()
//            items = nonEmptyListOf(
//                SearchLikedItemUiModelSample.Dexter,
//                SearchLikedItemUiModelSample.Grimm
//            )
    )

    val Idle = SearchLikedItemState(
        query = "",
        items = TODO()
    )

    val NoResults = SearchLikedItemState(
        query = "something",
        items = TODO()
    )

    val InitialLoading = SearchLikedItemState(
        query = "something",
        items = TODO()
    )

    val NoNetwork = SearchLikedItemState(
        query = "inception",
        items = TODO()
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
