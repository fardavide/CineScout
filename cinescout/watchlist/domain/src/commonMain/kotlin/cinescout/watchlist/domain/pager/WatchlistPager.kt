package cinescout.watchlist.domain.pager

import app.cash.paging.Pager
import arrow.core.Option
import cinescout.lists.domain.ListSorting
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.id.GenreSlug

interface WatchlistPager {

    fun create(
        genreFilter: Option<GenreSlug>,
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ): Pager<Int, Screenplay>
}
