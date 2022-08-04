package cinescout.store

sealed class PagedData<out T, out P : Paging> {

    abstract val data: List<T>
    abstract val paging: P

    abstract fun isLastPage(): Boolean

    abstract fun <R> map(transform: (T) -> R): PagedData<R, P>

    @PublishedApi
    internal data class Local<out T>(
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

fun <T> emptyPagedData(): PagedData.Remote<T, Paging.Page.SingleSource> = pagedDataOf()
inline fun <T> mergePagedData(
    first: PagedData.Remote<T, Paging.Page.SingleSource>,
    second: PagedData.Remote<T, Paging.Page.SingleSource>,
    id: (T) -> Any,
    onConflict: (first: T, second: T) -> T
): PagedData.Remote<T, Paging.Page.DualSources> {
    val data = run {
        val firstWithIds = first.data.associateBy(id)
        val secondWithIds = second.data.associateBy(id)

        val (firstOnly, firstIntersection) = firstWithIds.toList().partition { (id, _) -> id !in secondWithIds.keys }
        val (secondOnly, secondIntersection) = secondWithIds.toList().partition { (id, _) -> id !in firstWithIds.keys }
        val secondIntersectionMap = secondIntersection.toMap()
        val intersection = firstIntersection.map { (id, firstItem) ->
            val secondItem = secondIntersectionMap.getValue(id)
            onConflict(firstItem, secondItem)
        }

        firstOnly.map { it.second } + intersection + secondOnly.map { it.second }
    }
    return PagedData.Remote(
        data = data,
        paging = first.paging + second.paging
    )
}
fun <T> dualSourcesPagedDataOf(
    vararg items: T,
    paging: Paging.Page.DualSources = Paging.Page.DualSources.Initial
): PagedData.Remote<T, Paging.Page.DualSources> =
    PagedData.Remote(data = items.toList(), paging = paging)
fun <T> pagedDataOf(
    vararg items: T,
    paging: Paging.Page.SingleSource = Paging.Page.SingleSource.Initial
): PagedData.Remote<T, Paging.Page.SingleSource> =
    PagedData.Remote(data = items.toList(), paging = paging)
fun <T> List<T>.toPagedData(paging: Paging.Page) = PagedData.Remote(this, paging)
fun <T> List<T>.toPagedData(paging: Paging.Page.SingleSource) = PagedData.Remote(this, paging)
fun <T> List<T>.toPagedData(paging: Paging.Page.DualSources): PagedData.Remote<T, Paging.Page.DualSources> =
    PagedData.Remote(this, paging)
internal fun <T> List<T>.toPagedData() = PagedData.Local(this)
