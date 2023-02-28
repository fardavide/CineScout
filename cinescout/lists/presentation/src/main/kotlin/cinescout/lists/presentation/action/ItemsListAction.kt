package cinescout.lists.presentation.action

import cinescout.lists.presentation.model.ListFilter
import cinescout.lists.presentation.model.ListType

internal sealed interface ItemsListAction {

    class SelectFilter(val filter: ListFilter) : ItemsListAction
    class SelectType(val listType: ListType) : ItemsListAction
}
