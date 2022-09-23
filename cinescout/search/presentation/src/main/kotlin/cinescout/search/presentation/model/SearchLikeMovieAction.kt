package cinescout.search.presentation.model

import cinescout.movies.domain.model.TmdbMovieId

sealed interface SearchLikeMovieAction {

    data class LikeMovie(val movieId: TmdbMovieId) : SearchLikeMovieAction

    data class Search(val query: String) : SearchLikeMovieAction
}
