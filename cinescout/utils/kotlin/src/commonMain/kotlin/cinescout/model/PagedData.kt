package cinescout.model

sealed class PagedData<out T> {

    abstract val data: List<T>
    abstract val paging: Paging

    abstract fun isLastPage(): Boolean

    inline fun <R> map(transform: (T) -> R): PagedData<R> = when (this) {
        is Local -> Local(data = data.map(transform))
        is Remote -> Remote(data = data.map(transform), paging = paging)
    }

    @PublishedApi
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

fun <T> emptyPagedData(): PagedData.Remote<T> = pagedDataOf()
fun <T> pagedDataOf(vararg items: T): PagedData.Remote<T> =
    PagedData.Remote<T>(items.toList(), Paging.Page(1, 1))
fun <T> List<T>.toPagedData(paging: Paging.Page) = PagedData.Remote(this, paging)
internal fun <T> List<T>.toPagedData() = PagedData.Local(this)
