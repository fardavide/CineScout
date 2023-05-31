package cinescout.suggestions.presentation.action

import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import cinescout.suggestions.presentation.model.ForYouType

sealed interface ForYouAction {

    data class AddToWatchlist(val itemId: ScreenplayIds) : ForYouAction

    data class Dislike(val itemId: TmdbScreenplayId) : ForYouAction

    data class Like(val itemId: TmdbScreenplayId) : ForYouAction

    class SelectForYouType(val forYouType: ForYouType) : ForYouAction
}

