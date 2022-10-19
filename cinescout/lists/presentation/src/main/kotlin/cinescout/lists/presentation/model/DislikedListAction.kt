package cinescout.lists.presentation.model

sealed interface DislikedListAction {

    class SelectListType(val listType: ListType) : DislikedListAction
}
