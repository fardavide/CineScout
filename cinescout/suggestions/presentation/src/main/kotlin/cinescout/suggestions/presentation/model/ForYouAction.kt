package cinescout.suggestions.presentation.model

import cinescout.movies.domain.model.TmdbMovieId

sealed interface ForYouAction {

    data class AddToWatchlist(val movieId: TmdbMovieId) : ForYouAction
    data class Dislike(val movieId: TmdbMovieId) : ForYouAction
    object DismissHint : ForYouAction
    data class Like(val movieId: TmdbMovieId) : ForYouAction
}
