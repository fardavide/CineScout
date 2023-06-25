package cinescout.lists.presentation.model

import arrow.core.Option
import cinescout.lists.domain.ListSorting
import cinescout.screenplay.domain.model.Genre
import cinescout.screenplay.domain.model.ScreenplayTypeFilter

internal data class ListOptionUiModel(
    val genreFilter: Option<Genre>,
    val listFilter: ListFilter,
    val listSorting: ListSorting,
    val screenplayTypeFilter: ScreenplayTypeFilter
)
