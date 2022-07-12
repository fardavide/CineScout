package cinescout.auth.tmdb.data.local

import cinescout.auth.tmdb.data.TmdbAuthLocalDataSource
import cinescout.auth.tmdb.data.model.TmdbCredentials
import cinescout.network.tmdb.TmdbAuthProvider

internal class RealTmdbAuthProvider(
    private val dataSource: TmdbAuthLocalDataSource
) : TmdbAuthProvider {

    private var cachedCredentials: TmdbCredentials? = null

    override fun accessToken(): String? =
        getCredentials()?.accessToken?.value

    override fun accountId(): String? =
       getCredentials()?.accountId?.value

    override fun sessionId(): String? =
        getCredentials()?.sessionId?.value

    private fun getCredentials(): TmdbCredentials? =
        cachedCredentials ?: dataSource.findCredentialsBlocking()?.also { cachedCredentials = it }
}
