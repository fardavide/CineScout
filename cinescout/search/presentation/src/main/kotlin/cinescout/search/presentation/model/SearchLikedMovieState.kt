package cinescout.search.presentation.model

import arrow.core.NonEmptyList
import cinescout.design.TextRes

data class SearchLikedMovieState(
    val query: String,
    val result: SearchResult
) {

    sealed interface SearchResult {

        data class Data(val movies: NonEmptyList<SearchLikedMovieUiModel>) : SearchResult
        data class Error(val message: TextRes) : SearchResult
        object Idle : SearchResult
        object Loading : SearchResult
        object NoResults : SearchResult
    }

    companion object {

        val Idle = SearchLikedMovieState(
            query = "",
            result = SearchResult.Idle
        )
    }
}
