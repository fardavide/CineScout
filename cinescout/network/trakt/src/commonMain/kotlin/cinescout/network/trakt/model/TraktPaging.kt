package cinescout.network.trakt.model

import cinescout.lists.domain.PagingDefaults
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter

fun HttpRequestBuilder.withPaging(page: Int, limit: Int = PagingDefaults.PageSize) {
    parameter("page", page)
    parameter("limit", limit)
}

fun HttpRequestBuilder.withLimit(limit: Int) {
    parameter("limit", limit)
}

fun HttpRequestBuilder.noLimit() {
    withLimit(TraktPaging.NoLimit)
}

private object TraktPaging {

    const val NoLimit = Int.MAX_VALUE
}
