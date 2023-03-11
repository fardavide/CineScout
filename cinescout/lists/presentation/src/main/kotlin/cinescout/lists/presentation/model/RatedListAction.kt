package cinescout.lists.presentation.model

import cinescout.lists.domain.ListType

sealed interface RatedListAction {

    class SelectListType(val listType: ListType) : RatedListAction
}
