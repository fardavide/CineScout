package cinescout.watchlist.domain

import app.cash.paging.Pager
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType

interface WatchlistPager {

    fun create(listType: ScreenplayType): Pager<Int, Screenplay>
}

data class WatchlistPagerKey(val listType: ScreenplayType, val page: Int)
