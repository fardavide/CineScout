package cinescout.auth.tmdb.data.local

import cinescout.auth.tmdb.data.TmdbAuthLocalDataSource
import cinescout.auth.tmdb.data.model.TmdbCredentials
import cinescout.network.tmdb.TmdbAuthProvider
import org.koin.core.annotation.Single

@Single
internal class RealTmdbAuthProvider(
    private val dataSource: TmdbAuthLocalDataSource
) : TmdbAuthProvider {

    private var cachedCredentials: TmdbCredentials? = null

    override suspend fun accessToken(): String? =
        getCredentials()?.accessToken?.value

    override suspend fun accountId(): String? =
        getCredentials()?.accountId?.value

    override suspend fun sessionId(): String? =
        getCredentials()?.sessionId?.value

    private suspend fun getCredentials(): TmdbCredentials? =
        cachedCredentials ?: dataSource.findCredentials()?.also { cachedCredentials = it }
}
