package cinescout.lists.domain

import arrow.core.Option
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.id.GenreSlug

data class ListParams(
    val genreFilter: Option<GenreSlug>,
    val sorting: ListSorting,
    val type: ScreenplayTypeFilter
)
