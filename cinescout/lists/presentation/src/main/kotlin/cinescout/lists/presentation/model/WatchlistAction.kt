package cinescout.lists.presentation.model

sealed interface WatchlistAction {

    class SelectListType(val listType: ListType) : WatchlistAction
}
