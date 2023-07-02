package cinescout.rating.domain.pager

import app.cash.paging.Pager
import cinescout.lists.domain.ListParams
import cinescout.rating.domain.model.ScreenplayWithPersonalRating

interface RatingsPager {

    fun create(params: ListParams): Pager<Int, ScreenplayWithPersonalRating>
}
