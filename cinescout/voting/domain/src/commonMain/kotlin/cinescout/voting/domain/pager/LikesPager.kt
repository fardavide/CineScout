package cinescout.voting.domain.pager

import app.cash.paging.Pager
import arrow.core.Option
import cinescout.lists.domain.ListSorting
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.TmdbGenreId

interface LikesPager {

    fun create(
        genreFilter: Option<TmdbGenreId>,
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ): Pager<Int, Screenplay>
}
