package store

sealed interface Paging {

    object Unknown : Paging

    data class Page(
        val page: Int,
        val totalPages: Int
    ) : Paging {

        fun isFirstPage() = page == 1
        fun isInitial() = this == Initial
        fun isLastPage() = page >= totalPages
        fun isValid() = isInitial() || page <= totalPages

        operator fun plus(value: Int) = Page(
            page = page + value,
            totalPages = totalPages
        )

        companion object {

            val Initial = Page(page = 1, totalPages = 0)
            val Single = Page(page = 1, totalPages = 1)
        }
    }
}
