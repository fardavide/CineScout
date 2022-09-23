package cinescout.search.presentation.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import arrow.core.nonEmptyListOf
import cinescout.design.testdata.MessageTextResTestData
import cinescout.search.presentation.model.SearchLikedMovieState
import cinescout.search.presentation.testdata.SearchLikedMovieUiModelTestData

object SearchLikedMoviePreviewData {

    val Query_E = SearchLikedMovieState(
        query = "e",
        result = SearchLikedMovieState.SearchResult.Data(
            movies = nonEmptyListOf(
                SearchLikedMovieUiModelTestData.Inception,
                SearchLikedMovieUiModelTestData.TheWolfOfWallStreet
            )
        )
    )

    val Query_Inc = SearchLikedMovieState(
        query = "inc",
        result = SearchLikedMovieState.SearchResult.Data(
            movies = nonEmptyListOf(
                SearchLikedMovieUiModelTestData.Inception
            )
        )
    )

    val Idle = SearchLikedMovieState(
        query = "",
        result = SearchLikedMovieState.SearchResult.Idle
    )

    val NoResults = SearchLikedMovieState(
        query = "something",
        result = SearchLikedMovieState.SearchResult.NoResults
    )

    val Loading = SearchLikedMovieState(
        query = "something",
        result = SearchLikedMovieState.SearchResult.Loading
    )

    val NoNetwork = SearchLikedMovieState(
        query = "inception",
        result = SearchLikedMovieState.SearchResult.Error(message = MessageTextResTestData.NoNetworkError)
    )
}

class SearchLikedMoviePreviewDataProvider : PreviewParameterProvider<SearchLikedMovieState> {

    override val values = sequenceOf(
        SearchLikedMoviePreviewData.Query_E,
        SearchLikedMoviePreviewData.Query_Inc,
        SearchLikedMoviePreviewData.Idle,
        SearchLikedMoviePreviewData.NoResults,
        SearchLikedMoviePreviewData.Loading,
        SearchLikedMoviePreviewData.NoNetwork
    )
}
