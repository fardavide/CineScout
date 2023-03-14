package cinescout.auth.trakt.data.remote.testutil

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.http.fullPath
import io.ktor.http.headersOf

fun TraktAuthMockEngine() = MockEngine { requestData ->
    respond(
        content = getContent(requestData),
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
    )
}

private fun getContent(requestData: HttpRequestData): String {
    val path = requestData.url.fullPath
    (requestData.body as OutgoingContent.ByteArrayContent).bytes().decodeToString()
    return when {
        "oauth/token" in path -> {
            val bodyAsString = requestData.bodyAsString()
            when {
                """"grant_type":"authorization_code"""" in bodyAsString -> TraktAuthJson.AccessToken
                """"grant_type":"refresh_token"""" in bodyAsString -> TraktAuthJson.RefreshedAccessToken
                else -> throw IllegalArgumentException("Unknown request body: $bodyAsString")
            }
        }
        else -> throw UnsupportedOperationException(path)
    }
}

private fun HttpRequestData.bodyAsString(): String =
    (body as OutgoingContent.ByteArrayContent).bytes().decodeToString()
