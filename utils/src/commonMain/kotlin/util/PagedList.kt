package util

/**
 * A [List] build from paged result
 * The only difference from a [List] is that is holds the number of total pages and the pages currently loaded
 */
interface PagedList<T> : List<T> {

    /**
     * Count of pages already loaded
     */
    val currentPage: Int

    /**
     * Count of all the pages available
     */
    val totalPages: Int

    fun hasMorePages(): Boolean =
        currentPage < totalPages
}

fun <T> PagedList(collection: Collection<T>, currentPage: Int, totalPages: Int): PagedList<T> =
    PagedListImpl(collection.toList(), currentPage, totalPages)

fun <T> Collection<T>.toPagedList(currentPage: Int, totalPages: Int): PagedList<T> =
    PagedList(this, currentPage, totalPages)


private class PagedListImpl<T>(
    underlying: List<T>,
    override val currentPage: Int,
    override val totalPages: Int
) : PagedList<T>, List<T> by underlying

