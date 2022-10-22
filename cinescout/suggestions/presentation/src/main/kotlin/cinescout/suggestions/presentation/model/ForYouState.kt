package cinescout.suggestions.presentation.model

import cinescout.design.TextRes

data class ForYouState(
    val shouldShowHint: Boolean,
    val suggestedMovie: SuggestedMovie,
    val suggestedTvShow: SuggestedTvShow
) {
    
    sealed interface SuggestedMovie {

        data class Data(val movie: ForYouMovieUiModel) : SuggestedMovie
        data class Error(val message: TextRes) : SuggestedMovie
        object Loading : SuggestedMovie
        object NoSuggestions : SuggestedMovie
    }

    sealed interface SuggestedTvShow {

        data class Data(val tvShow: ForYouTvShowUiModel) : SuggestedTvShow
        data class Error(val message: TextRes) : SuggestedTvShow
        object Loading : SuggestedTvShow
        object NoSuggestions : SuggestedTvShow
    }

    companion object {

        val Loading = ForYouState(
            shouldShowHint = false,
            suggestedMovie = SuggestedMovie.Loading,
            suggestedTvShow = SuggestedTvShow.Loading
        )
    }
}
