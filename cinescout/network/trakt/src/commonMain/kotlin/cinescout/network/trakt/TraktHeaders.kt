package cinescout.network.trakt

import cinescout.store.Paging
import io.ktor.http.Headers

object TraktHeaders {

    object Pagination {

        const val Page = "X-Pagination-Page"
        const val TotalPages = "X-Pagination-Page-Count"
    }
}

fun Headers.getPaging() = Paging.Page(
    page = checkNotNull(this[TraktHeaders.Pagination.Page]) { "Page is null" }.toInt(),
    totalPages = checkNotNull(this[TraktHeaders.Pagination.TotalPages]) { "TotalPages is null" }.toInt()
)
