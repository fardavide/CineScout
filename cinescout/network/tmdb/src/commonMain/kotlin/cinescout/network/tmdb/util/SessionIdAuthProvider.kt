package cinescout.network.tmdb.util

import cinescout.network.tmdb.TmdbParameters
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.AuthProvider
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.util.KtorDsl

/**
 * Installs the client's [SessionIdAuthProvider].
 */
@KtorDsl
public fun Auth.sessionId(block: SessionIdAuthConfig.() -> Unit) {
    with(SessionIdAuthConfig().apply(block)) {
        this@sessionId.providers.add(SessionIdAuthProvider(sessionId))
    }
}

/**
 * A configuration for [SessionIdAuthProvider].
 */
@KtorDsl
class SessionIdAuthConfig {

    internal var sessionId: suspend () -> String? = { null }

    /**
     * Allows you to specify authentication credentials.
     */
    public fun sessionId(block: suspend () -> String?) {
        sessionId = block
    }
}

class SessionIdAuthProvider(
    private val sessionId: suspend () -> String?
) : AuthProvider {

    @Deprecated(
        "Please use sendWithoutRequest function instead",
        ReplaceWith("sendWithoutRequest(request)")
    )
    override val sendWithoutRequest: Boolean
        get() = false

    override suspend fun addRequestHeaders(request: HttpRequestBuilder, authHeader: HttpAuthHeader?) {
        sessionId()?.let { sessionId ->
            request.url.parameters.append(TmdbParameters.SessionId, sessionId)
        }
    }

    override fun isApplicable(auth: HttpAuthHeader) = true
}
