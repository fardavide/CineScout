package cinescout.suggestions.presentation.model

import cinescout.design.TextRes

data class ForYouState(
    val loggedIn: ForYouState.LoggedIn,
    val suggestedMovie: SuggestedMovie
) {

    sealed interface LoggedIn {

        object Loading : LoggedIn
        object True : LoggedIn
        object False : LoggedIn
    }

    sealed interface SuggestedMovie {

        data class Data(val movie: ForYouMovieUiModel) : SuggestedMovie
        data class Error(val message: TextRes) : SuggestedMovie
        object Loading : SuggestedMovie
        object NoSuggestions : SuggestedMovie
    }

    companion object {

        val Loading = ForYouState(
            loggedIn = LoggedIn.Loading,
            suggestedMovie = SuggestedMovie.Loading
        )
    }
}
