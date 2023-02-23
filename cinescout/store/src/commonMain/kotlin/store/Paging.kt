package store

sealed interface Paging {

    operator fun plus(other: Paging): Paging = if (this is Page && other is Page) {
        Page(
            page = page + other.page,
            totalPages = totalPages + other.totalPages
        )
    } else {
        Unknown
    }

    object Unknown : Paging

    data class Page(
        val page: Int,
        val totalPages: Int
    ) : Paging {

        fun isFirstPage() = page == 1
        fun isLastPage() = page >= totalPages
        fun isValid() = page in 1..totalPages

        operator fun plus(value: Int) = Page(
            page = page + value,
            totalPages = totalPages
        )

        companion object {

            val Initial = Page(page = 1, totalPages = 1)
        }
    }
}

@PublishedApi
internal inline fun <reified P : Paging> Initial(): P = when (P::class) {
    Paging.Page::class -> Paging.Page.Initial
    Paging.Unknown::class -> Paging.Unknown
    else -> throw IllegalArgumentException("Unknown paging type")
} as P
