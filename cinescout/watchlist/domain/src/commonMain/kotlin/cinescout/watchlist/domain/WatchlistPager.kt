package cinescout.watchlist.domain

import app.cash.paging.Pager
import cinescout.lists.domain.ListType
import cinescout.screenplay.domain.model.Screenplay

interface WatchlistPager {

    fun create(listType: ListType): Pager<Int, Screenplay>
}

data class WatchlistPagerKey(val listType: ListType, val page: Int)
