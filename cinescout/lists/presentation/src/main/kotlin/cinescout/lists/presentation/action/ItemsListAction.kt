package cinescout.lists.presentation.action

import cinescout.lists.presentation.model.ListFilter
import cinescout.screenplay.domain.model.ScreenplayType

internal sealed interface ItemsListAction {

    class SelectFilter(val filter: ListFilter) : ItemsListAction
    class SelectType(val listType: ScreenplayType) : ItemsListAction
}
