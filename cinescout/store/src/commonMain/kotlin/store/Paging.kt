package store

sealed interface Paging {

    object Unknown : Paging

    data class Page(
        val page: Int,
        val totalPages: Int
    ) : Paging {

        fun isFirstPage() = page == 1
        fun isLastPage() = page >= totalPages

        operator fun plus(value: Int) = Page(
            page = page + value,
            totalPages = totalPages
        )

        companion object {

            val Initial = Page(page = 1, totalPages = 1)
        }
    }
}
