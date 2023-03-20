package cinescout.suggestions.presentation.state

import cinescout.resources.TextRes
import cinescout.suggestions.presentation.model.ForYouScreenplayUiModel
import cinescout.suggestions.presentation.model.ForYouType

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
