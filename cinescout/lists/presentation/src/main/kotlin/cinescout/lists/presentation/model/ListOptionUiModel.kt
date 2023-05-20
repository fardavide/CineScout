package cinescout.lists.presentation.model

import cinescout.lists.domain.ListSorting
import cinescout.screenplay.domain.model.ScreenplayTypeFilter

internal data class ListOptionUiModel(
    val listFilter: ListFilter,
    val listSorting: ListSorting,
    val screenplayTypeFilter: ScreenplayTypeFilter
)
