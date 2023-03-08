package cinescout.network.testutil

import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpResponseData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

fun MockRequestHandleScope.respondJson(
    content: String,
    otherHeaders: Headers = headersOf()
): HttpResponseData = respond(
    content = content,
    status = HttpStatusCode.OK,
    headers = Headers.build {
        appendAll(otherHeaders)
        append(HttpHeaders.ContentType, "application/json")
    }
)

fun HttpMethod.isGet() = this == HttpMethod.Get
