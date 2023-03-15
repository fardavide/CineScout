package cinescout.voting.domain.pager

import app.cash.paging.Pager
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType

interface DislikesPager {

    fun create(listType: ScreenplayType): Pager<Int, Screenplay>
}
