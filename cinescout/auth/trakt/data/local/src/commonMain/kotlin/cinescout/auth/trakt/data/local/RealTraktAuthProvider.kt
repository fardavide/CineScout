package cinescout.auth.trakt.data.local

import cinescout.auth.trakt.data.TraktAuthLocalDataSource
import cinescout.auth.trakt.data.model.TraktAccessAndRefreshTokens
import cinescout.network.trakt.TraktAuthProvider
import org.koin.core.annotation.Single

@Single
class RealTraktAuthProvider(
    private val dataSource: TraktAuthLocalDataSource
) : TraktAuthProvider {

    private var cachedTokens: TraktAccessAndRefreshTokens? = null

    override suspend fun accessToken(): String? =
        getTokens()?.accessToken?.value

    override suspend fun refreshToken(): String? =
        getTokens()?.refreshToken?.value

    private suspend fun getTokens(): TraktAccessAndRefreshTokens? =
        cachedTokens ?: dataSource.findTokens()?.also { cachedTokens = it }
}
