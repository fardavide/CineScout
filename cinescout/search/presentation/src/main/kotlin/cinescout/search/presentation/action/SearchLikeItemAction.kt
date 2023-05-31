package cinescout.search.presentation.action

import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import cinescout.search.presentation.model.SearchLikedItemType

sealed interface SearchLikeItemAction {

    data class LikeItem(val itemId: TmdbScreenplayId) : SearchLikeItemAction

    data class Search(val query: String) : SearchLikeItemAction

    class SelectItemType(val itemType: SearchLikedItemType) : SearchLikeItemAction
}
