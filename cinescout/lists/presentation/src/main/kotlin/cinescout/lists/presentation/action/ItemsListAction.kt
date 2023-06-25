package cinescout.lists.presentation.action

import arrow.core.Option
import cinescout.lists.domain.ListSorting
import cinescout.lists.presentation.model.ListFilter
import cinescout.screenplay.domain.model.Genre
import cinescout.screenplay.domain.model.ScreenplayTypeFilter

internal sealed interface ItemsListAction {

    data class SelectGenreFilter(val genre: Option<Genre>) : ItemsListAction
    data class SelectListFilter(val filter: ListFilter) : ItemsListAction
    data class SelectSorting(val sorting: ListSorting) : ItemsListAction
    data class SelectType(val listType: ScreenplayTypeFilter) : ItemsListAction
}
