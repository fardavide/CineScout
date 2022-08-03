package cinescout.suggestions.presentation.model

import arrow.core.NonEmptyList
import cinescout.design.TextRes
import cinescout.movies.domain.model.Movie

data class ForYouState(
    val suggestedMovies: SuggestedMovies
) {

    sealed interface SuggestedMovies {

        data class Data(val movies: NonEmptyList<Movie>) : SuggestedMovies
        data class Error(val message: TextRes) : SuggestedMovies
        object Loading : SuggestedMovies
        object NoSuggestions : SuggestedMovies
    }

    companion object {

        val Loading = ForYouState(
            suggestedMovies = SuggestedMovies.Loading
        )
    }
}
