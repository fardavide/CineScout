package cinescout.lists.presentation.model

sealed interface LikedListAction {

    class SelectListType(val listType: ListType) : LikedListAction
}
