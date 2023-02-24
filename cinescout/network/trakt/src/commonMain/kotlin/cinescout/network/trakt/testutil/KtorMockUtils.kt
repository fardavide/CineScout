package cinescout.network.trakt.testutil

import cinescout.network.testutil.respondJson
import cinescout.network.trakt.TraktHeaders
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.request.HttpResponseData
import io.ktor.http.Headers
import io.ktor.http.headersOf

fun MockRequestHandleScope.respondTraktJsonPage(
    content: String,
    otherHeaders: Headers = headersOf(),
    page: Int = 1,
    totalPages: Int = 1
): HttpResponseData = respondJson(
    content = content,
    otherHeaders = Headers.build {
        appendAll(otherHeaders)
        append(TraktHeaders.Pagination.Page, "$page")
        append(TraktHeaders.Pagination.TotalPages, "$totalPages")
    }
)
