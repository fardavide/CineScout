package cinescout.watchlist.domain.pager

import app.cash.paging.Pager
import cinescout.lists.domain.ListSorting
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType

interface WatchlistPager {

    fun create(sorting: ListSorting, type: ScreenplayType): Pager<Int, Screenplay>
}
