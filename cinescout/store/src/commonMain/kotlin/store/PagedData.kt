package store

sealed class PagedData<out T, out P : Paging> {

    abstract val data: List<T>
    abstract val paging: P

    abstract fun isLastPage(): Boolean

    abstract fun <R> map(transform: (T) -> R): PagedData<R, P>

    data class Local<out T>(
        override val data: List<T>
    ) : PagedData<T, Paging.Unknown>() {

        override val paging = Paging.Unknown

        override fun isLastPage() = false

        override fun <R> map(transform: (T) -> R): PagedData<R, Paging.Unknown> =
            Local(data.map(transform))
    }

    data class Remote<out T, out P : Paging.Page>(
        override val data: List<T>,
        override val paging: P
    ) : PagedData<T, P>() {

        fun isFirstPage(): Boolean = paging.isFirstPage()
        override fun isLastPage(): Boolean = paging.isLastPage()

        @Suppress("OVERRIDE_BY_INLINE")
        override inline fun <R> map(transform: (T) -> R): Remote<R, P> =
            Remote(data = data.map(transform), paging = paging)
    }
}
