package cinescout.lists.presentation.model

sealed interface RatedListAction {

    class SelectListType(val listType: ListType) : RatedListAction
}
