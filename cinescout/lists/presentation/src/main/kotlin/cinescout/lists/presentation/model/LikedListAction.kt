package cinescout.lists.presentation.model

import cinescout.lists.domain.ListType

sealed interface LikedListAction {

    class SelectListType(val listType: ListType) : LikedListAction
}
