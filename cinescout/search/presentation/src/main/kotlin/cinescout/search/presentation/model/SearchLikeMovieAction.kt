package cinescout.search.presentation.model

sealed interface SearchLikeMovieAction {

    data class Search(val query: String) : SearchLikeMovieAction
}
