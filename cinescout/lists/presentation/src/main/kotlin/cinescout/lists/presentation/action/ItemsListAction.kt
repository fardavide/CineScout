package cinescout.lists.presentation.action

import cinescout.lists.domain.ListType
import cinescout.lists.presentation.model.ListFilter

internal sealed interface ItemsListAction {

    class SelectFilter(val filter: ListFilter) : ItemsListAction
    class SelectType(val listType: ListType) : ItemsListAction
}
