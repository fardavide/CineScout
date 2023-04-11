package cinescout.rating.domain.pager

import app.cash.paging.Pager
import cinescout.lists.domain.ListSorting
import cinescout.rating.domain.model.ScreenplayWithPersonalRating
import cinescout.screenplay.domain.model.ScreenplayType

interface RatingsPager {

    fun create(sorting: ListSorting, type: ScreenplayType): Pager<Int, ScreenplayWithPersonalRating>
}
