package cinescout.lists.data.local.mapper

import cinescout.lists.domain.ListSorting
import cinescout.lists.domain.SortingDirection
import org.koin.core.annotation.Factory

@Factory
class DatabaseListSortingMapper {

    fun toDatabaseQuery(sorting: ListSorting): String = when (sorting) {
        is ListSorting.Rating -> when (sorting.direction) {
            SortingDirection.Ascending -> "ratingAverage_ASC"
            SortingDirection.Descending -> "ratingAverage_DESC"
        }
        is ListSorting.ReleaseDate -> when (sorting.direction) {
            SortingDirection.Ascending -> "releaseDate_ASC"
            SortingDirection.Descending -> "releaseDate_DESC"
        }
    }
}
