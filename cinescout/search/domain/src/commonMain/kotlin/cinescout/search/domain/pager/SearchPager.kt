package cinescout.search.domain.pager

import app.cash.paging.Pager
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter

interface SearchPager {

    fun create(type: ScreenplayTypeFilter, query: String): Pager<Int, Screenplay>
}
