package cinescout.suggestions.presentation.model

import cinescout.design.TextRes

internal data class ForYouState(
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
            suggestedItem = SuggestedItem.Loading,
            type = ForYouType.Movies
        )
    }
}
