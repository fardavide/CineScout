package cinescout.rating.domain.pager

import app.cash.paging.Pager
import arrow.core.Option
import cinescout.lists.domain.ListSorting
import cinescout.rating.domain.model.ScreenplayWithPersonalRating
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.TmdbGenreId

interface RatingsPager {

    fun create(
        genreFilter: Option<TmdbGenreId>,
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ): Pager<Int, ScreenplayWithPersonalRating>
}
