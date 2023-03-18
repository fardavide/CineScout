package cinescout.search.domain.pager

import app.cash.paging.Pager
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType

interface SearchPager {

    fun create(type: ScreenplayType, query: String): Pager<Int, Screenplay>
}
