package store

sealed interface Paging {

    operator fun plus(other: Paging): Paging =
        if (this is Page && other is Page) {
            Page(
                page = page + other.page,
                totalPages = totalPages + other.totalPages
            )
        } else {
            Unknown
        }

    object Unknown : Paging

    sealed interface Page : Paging {
        val page: Int
        val totalPages: Int

        fun isFirstPage(): Boolean
        fun isLastPage(): Boolean

        operator fun plus(value: Int): Page {
            return when (this) {
                is DualSources -> this + 1
                is SingleSource -> this + 1
            }
        }

        data class SingleSource(
            override val page: Int,
            override val totalPages: Int
        ) : Page {

            override fun isFirstPage() = page == 1
            override fun isLastPage() = page >= totalPages
            fun isValid() = page in 1..totalPages

            operator fun plus(other: SingleSource) = DualSources(
                first = this,
                second = other
            )

            override operator fun plus(value: Int) = SingleSource(
                page = page + 1,
                totalPages = totalPages
            )

            companion object {

                val Initial = SingleSource(page = 1, totalPages = 1)
            }
        }

        data class DualSources(
            val first: SingleSource,
            val second: SingleSource
        ) : Page {

            override val page: Int
                get() = first.page + second.page

            override val totalPages: Int
                get() = first.totalPages + second.totalPages

            override fun isFirstPage() = first.isFirstPage() && second.isFirstPage()
            override fun isLastPage() = first.isLastPage() && second.isLastPage()

            override operator fun plus(value: Int) = DualSources(
                first = first + 1,
                second = second + 1
            )

            companion object {

                val Initial = DualSources(first = SingleSource.Initial, second = SingleSource.Initial)
            }
        }

        companion object {

            operator fun invoke(page: Int, totalPages: Int) = SingleSource(page, totalPages)
        }
    }
}
