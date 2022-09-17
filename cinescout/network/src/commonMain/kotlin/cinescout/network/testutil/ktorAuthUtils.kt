package cinescout.network.testutil

import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpHeaders

fun HttpRequestData.hasValidAccessToken() =
    headers[HttpHeaders.Authorization] != null
