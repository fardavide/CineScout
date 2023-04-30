package cinescout.voting.domain.pager

import app.cash.paging.Pager
import cinescout.lists.domain.ListSorting
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter

interface DislikesPager {

    fun create(sorting: ListSorting, type: ScreenplayTypeFilter): Pager<Int, Screenplay>
}
