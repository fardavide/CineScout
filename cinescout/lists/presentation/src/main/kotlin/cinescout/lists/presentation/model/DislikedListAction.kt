package cinescout.lists.presentation.model

import cinescout.lists.domain.ListType

sealed interface DislikedListAction {

    class SelectListType(val listType: ListType) : DislikedListAction
}
