package cinescout.lists.data.remote.mapper

import cinescout.lists.domain.ListSorting
import cinescout.lists.domain.SortingDirection
import cinescout.network.trakt.model.TraktSort
import cinescout.network.trakt.model.TraktSortBy
import cinescout.network.trakt.model.TraktSortHow
import org.koin.core.annotation.Factory

@Factory
class TraktListSortingMapper {

    fun toTraktSort(listSorting: ListSorting) = when (listSorting) {
        is ListSorting.Rating -> when (listSorting.direction) {
            SortingDirection.Ascending -> TraktSort(TraktSortBy.Rank, TraktSortHow.Asc)
            SortingDirection.Descending -> TraktSort(TraktSortBy.Rank, TraktSortHow.Desc)
        }
        is ListSorting.ReleaseDate -> when (listSorting.direction) {
            SortingDirection.Ascending -> TraktSort(TraktSortBy.Released, TraktSortHow.Asc)
            SortingDirection.Descending -> TraktSort(TraktSortBy.Released, TraktSortHow.Desc)
        }
    }
}
