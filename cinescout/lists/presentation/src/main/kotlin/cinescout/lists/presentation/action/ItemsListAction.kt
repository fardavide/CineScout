package cinescout.lists.presentation.action

import cinescout.lists.domain.ListSorting
import cinescout.lists.presentation.model.ListFilter
import cinescout.screenplay.domain.model.ScreenplayType

internal sealed interface ItemsListAction {

    data class SelectFilter(val filter: ListFilter) : ItemsListAction
    data class SelectSorting(val sorting: ListSorting) : ItemsListAction
    data class SelectType(val listType: ScreenplayType) : ItemsListAction
}
