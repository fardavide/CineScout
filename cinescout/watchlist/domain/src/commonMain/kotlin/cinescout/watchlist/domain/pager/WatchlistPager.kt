package cinescout.watchlist.domain.pager

import app.cash.paging.Pager
import cinescout.lists.domain.ListParams
import cinescout.screenplay.domain.model.Screenplay

interface WatchlistPager {

    fun create(params: ListParams): Pager<Int, Screenplay>
}
