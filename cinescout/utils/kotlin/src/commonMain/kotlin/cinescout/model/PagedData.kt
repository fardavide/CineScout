package cinescout.model

sealed class PagedData<out T> {

    abstract val data: List<T>
    abstract val paging: Paging

    abstract fun isLastPage(): Boolean

    internal data class Local<out T>(
        override val data: List<T>
    ) : PagedData<T>() {

        override val paging: Paging = Paging.Unknown

        override fun isLastPage() = false
    }

    data class Remote<out T>(
        override val data: List<T>,
        override val paging: Paging.Page
    ) : PagedData<T>() {

        fun isFirstPage(): Boolean = paging.page == 1
        override fun isLastPage(): Boolean = paging.page == paging.totalPages
    }
}

sealed interface Paging {

    object Unknown : Paging

    data class Page(
        val page: Int,
        val totalPages: Int
    ) : Paging
}

internal fun <T> List<T>.toPagedData() = PagedData.Local(this)
