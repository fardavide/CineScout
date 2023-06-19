package cinescout.suggestions.presentation.action

import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.suggestions.presentation.model.ForYouType

sealed interface ForYouAction {

    data class AddToWatchlist(val itemId: ScreenplayIds) : ForYouAction

    data class Dislike(val itemId: ScreenplayIds) : ForYouAction

    data class Like(val itemId: ScreenplayIds) : ForYouAction

    class SelectForYouType(val forYouType: ForYouType) : ForYouAction
}

