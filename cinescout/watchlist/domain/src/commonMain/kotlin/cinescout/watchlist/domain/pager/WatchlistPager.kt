package cinescout.watchlist.domain.pager

import app.cash.paging.Pager
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType

interface WatchlistPager {

    fun create(type: ScreenplayType): Pager<Int, Screenplay>
}
