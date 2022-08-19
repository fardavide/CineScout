package cinescout.network.trakt

import io.ktor.http.Headers
import store.Paging

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
