package cinescout.suggestions.presentation.model

import cinescout.design.TextRes
import cinescout.suggestions.presentation.util.Stack

internal data class ForYouState(
    val moviesStack: Stack<ForYouScreenplayUiModel>,
    val tvShowsStack: Stack<ForYouScreenplayUiModel>,
    val suggestedItem: SuggestedItem,
    val type: ForYouType
) {

    sealed interface SuggestedItem {

        data class Error(val message: TextRes) : SuggestedItem

        object Loading : SuggestedItem

        object NoSuggestedMovies : SuggestedItem

        object NoSuggestedTvShows : SuggestedItem

        data class Screenplay(val screenplay: ForYouScreenplayUiModel) : SuggestedItem
    }

    companion object {

        val Loading = ForYouState(
            moviesStack = Stack.empty(),
            tvShowsStack = Stack.empty(),
            suggestedItem = SuggestedItem.Loading,
            type = ForYouType.Movies
        )
    }
}
