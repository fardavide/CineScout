package cinescout.search.presentation.action

import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.search.presentation.model.SearchLikedItemType

sealed interface SearchLikeItemAction {

    data class LikeItem(val itemId: ScreenplayIds) : SearchLikeItemAction

    data class Search(val query: String) : SearchLikeItemAction

    class SelectItemType(val itemType: SearchLikedItemType) : SearchLikeItemAction
}
