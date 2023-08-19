package cinescout.suggestions.presentation.state

import cinescout.resources.TextRes
import cinescout.suggestions.presentation.model.ForYouScreenplayUiModel
import cinescout.suggestions.presentation.model.ForYouType

internal data class ForYouState(
    val suggestedItem: SuggestedItem,
    val type: ForYouType
) {

    sealed interface SuggestedItem {

        @JvmInline
        value class Error(val message: TextRes) : SuggestedItem

        data object Loading : SuggestedItem

        data object NoSuggestedMovies : SuggestedItem

        data object NoSuggestedTvShows : SuggestedItem

        @JvmInline
        value class Screenplay(val screenplay: ForYouScreenplayUiModel) : SuggestedItem
    }

    companion object {

        val Loading = ForYouState(
            suggestedItem = SuggestedItem.Loading,
            type = ForYouType.Movies
        )
    }
}
