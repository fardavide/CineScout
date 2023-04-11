package cinescout.network.trakt.model

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers

data class TraktSort(
    val by: TraktSortBy,
    val how: TraktSortHow
)

enum class TraktSortBy(val value: String) {

    Rank("rank")
}

enum class TraktSortHow(val value: String) {

    Asc("asc"),
    Desc("desc")
}

fun HttpRequestBuilder.sort(sort: TraktSort) {
    headers {
        append("X-Sort-By", sort.by.value)
        append("X-Sort-How", sort.how.value)
    }
}
