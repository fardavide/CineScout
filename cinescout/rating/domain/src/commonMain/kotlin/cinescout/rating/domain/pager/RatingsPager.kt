package cinescout.rating.domain.pager

import app.cash.paging.Pager
import cinescout.rating.domain.model.ScreenplayWithPersonalRating
import cinescout.screenplay.domain.model.ScreenplayType

interface RatingsPager {

    fun create(listType: ScreenplayType): Pager<Int, ScreenplayWithPersonalRating>
}
