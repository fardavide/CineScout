package cinescout.store

sealed class PagedData<T> {

    abstract val data: List<T>
    abstract val paging: Paging

    abstract fun isLastPage(): Boolean

    abstract fun <R> map(transform: (T) -> R): PagedData<R>

    @PublishedApi
    internal data class Local<T>(
        override val data: List<T>
    ) : PagedData<T>() {

        override val paging: Paging = Paging.Unknown

        override fun isLastPage() = false

        override fun <R> map(transform: (T) -> R): Local<R> =
            Local(data = data.map(transform))
    }

    data class Remote<T>(
        override val data: List<T>,
        override val paging: Paging.Page
    ) : PagedData<T>() {

        fun isFirstPage(): Boolean = paging.isFirstPage()
        override fun isLastPage(): Boolean = paging.page == paging.totalPages

        @Suppress("OVERRIDE_BY_INLINE")
        override inline fun <R> map(transform: (T) -> R): Remote<R> =
            Remote(data = data.map(transform), paging = paging)

        operator fun plus(other: Remote<T>): Remote<T> =
            Remote(
                data = data + other.data,
                paging = paging + other.paging
            )
    }

    operator fun plus(other: PagedData<T>): PagedData<T> =
        if (this is Remote && other is Remote<T>) Remote(data = data + other.data, paging = paging + other.paging)
        else Local(data = data + other.data)
}

sealed interface Paging {

    object Unknown : Paging

    sealed interface Page : Paging {
        val page: Int
        val totalPages: Int

        fun isFirstPage(): Boolean

        operator fun plus(other: Page): Page = MultipleSources(
            page = page + other.page,
            totalPages = totalPages + other.totalPages
        )

        data class SingleSource(
            override val page: Int,
            override val totalPages: Int
        ) : Page {

            override fun isFirstPage() = page == 1
        }

        data class MultipleSources(
            override val page: Int,
            override val totalPages: Int
        ) : Page {

            override fun isFirstPage() = page == 2
        }

        companion object {

            operator fun invoke(page: Int, totalPages: Int): Page = SingleSource(page, totalPages)
        }
    }

    operator fun plus(other: Paging): Paging =
        if (this is Page && other is Page) {
            Page(
                page = page + other.page,
                totalPages = totalPages + other.totalPages
            )
        } else {
            Unknown
        }
}

fun <T> emptyPagedData(): PagedData.Remote<T> = pagedDataOf()
inline fun <T> mergePagedData(
    first: PagedData.Remote<T>,
    second: PagedData.Remote<T>,
    id: (T) -> Any,
    onConflict: (first: T, second: T) -> T
): PagedData.Remote<T> {
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
fun <T> multipleSourcesPagedDataOf(vararg items: T): PagedData.Remote<T> =
    PagedData.Remote(items.toList(), Paging.Page.MultipleSources(2, 2))
fun <T> pagedDataOf(vararg items: T, paging: Paging.Page = Paging.Page(1, 1)): PagedData.Remote<T> =
    PagedData.Remote(items.toList(), paging)
fun <T> List<T>.toPagedData(paging: Paging.Page) = PagedData.Remote(this, paging)
internal fun <T> List<T>.toPagedData() = PagedData.Local(this)
