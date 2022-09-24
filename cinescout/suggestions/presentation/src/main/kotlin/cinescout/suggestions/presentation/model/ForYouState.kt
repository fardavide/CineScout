package cinescout.suggestions.presentation.model

import cinescout.design.TextRes

data class ForYouState(
    val shouldShowHint: Boolean,
    val suggestedMovie: SuggestedMovie
) {
    
    sealed interface SuggestedMovie {

        data class Data(val movie: ForYouMovieUiModel) : SuggestedMovie
        data class Error(val message: TextRes) : SuggestedMovie
        object Loading : SuggestedMovie
        object NoSuggestions : SuggestedMovie
    }

    companion object {

        val Loading = ForYouState(
            shouldShowHint = false,
            suggestedMovie = SuggestedMovie.Loading
        )
    }
}
