package cinescout.lists.presentation.model

import cinescout.lists.domain.ListType

sealed interface WatchlistAction {

    class SelectListType(val listType: ListType) : WatchlistAction
}
