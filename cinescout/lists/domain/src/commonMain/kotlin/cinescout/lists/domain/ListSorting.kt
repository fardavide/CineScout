package cinescout.lists.domain

sealed interface ListSorting {

    val direction: SortingDirection

    data class Rating(override val direction: SortingDirection) : ListSorting {

        companion object {

            val Ascending = Rating(SortingDirection.Ascending)
            val Descending = Rating(SortingDirection.Descending)
        }
    }

    data class ReleaseDate(override val direction: SortingDirection) : ListSorting {

        companion object {

            val Ascending = ReleaseDate(SortingDirection.Ascending)
            val Descending = ReleaseDate(SortingDirection.Descending)
        }
    }
}
