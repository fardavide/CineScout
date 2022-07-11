package cinescout.auth.tmdb.data.local

import cinescout.auth.tmdb.data.TmdbAuthLocalDataSource
import cinescout.network.tmdb.TmdbAuthProvider

internal class RealTmdbAuthProvider(
    private val dataSource: TmdbAuthLocalDataSource
) : TmdbAuthProvider {

    private var cachedAccessToken: String? = null
    private var cachedSessionId: String? = null
    
    override fun accessToken(): String? =
        cachedAccessToken ?: dataSource.findCredentialsBlocking()?.accessToken?.value.also { cachedAccessToken = it
        }

    override fun sessionId(): String? =
        cachedSessionId ?: dataSource.findCredentialsBlocking()?.sessionId?.value.also {
            cachedSessionId = it
        }
}
