package cinescout.search.presentation.model

import arrow.core.NonEmptyList
import arrow.core.Option
import arrow.core.none
import arrow.core.some
import cinescout.design.TextRes

data class SearchLikedMovieState(
    val query: String,
    val result: SearchResult
) {

    sealed interface SearchResult {

        fun movies(): Option<NonEmptyList<SearchLikedMovieUiModel>> =
            when (this) {
                is Data -> movies.some()
                is Loading -> previousMovies
                else -> none()
            }

        fun onNewQuery(query: String): SearchResult =
            when {
                query.isBlank() -> Idle
                this is Data -> Loading(previousMovies = movies.some())
                else -> Loading(previousMovies = none())
            }

        data class Data(val movies: NonEmptyList<SearchLikedMovieUiModel>) : SearchResult
        data class Error(val message: TextRes) : SearchResult
        object Idle : SearchResult
        data class Loading(val previousMovies: Option<NonEmptyList<SearchLikedMovieUiModel>>) : SearchResult
        object NoResults : SearchResult
    }

    companion object {

        val Idle = SearchLikedMovieState(
            query = "",
            result = SearchResult.Idle
        )
    }
}
