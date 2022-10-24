package cinescout.suggestions.presentation.model

import cinescout.design.TextRes
import cinescout.suggestions.presentation.util.Stack

internal data class ForYouState(
    val moviesStack: Stack<ForYouMovieUiModel>,
    val tvShowsStack: Stack<ForYouTvShowUiModel>,
    val shouldShowHint: Boolean,
    val suggestedItem: SuggestedItem,
    val type: ForYouType
) {

    sealed interface SuggestedItem {

        data class Error(val message: TextRes) : SuggestedItem

        object Loading : SuggestedItem

        data class Movie(val movie: ForYouMovieUiModel) : SuggestedItem

        object NoSuggestedMovies : SuggestedItem

        object NoSuggestedTvShows : SuggestedItem

        data class TvShow(val tvShow: ForYouTvShowUiModel) : SuggestedItem
    }

    companion object {

        val Loading = ForYouState(
            moviesStack = Stack.empty(),
            tvShowsStack = Stack.empty(),
            shouldShowHint = false,
            suggestedItem = SuggestedItem.Loading,
            type = ForYouType.Movies
        )
    }
}
