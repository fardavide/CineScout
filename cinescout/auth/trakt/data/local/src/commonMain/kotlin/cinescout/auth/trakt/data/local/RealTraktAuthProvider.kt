package cinescout.auth.trakt.data.local

import cinescout.auth.trakt.data.TraktAuthLocalDataSource
import cinescout.auth.trakt.data.model.TraktAccessAndRefreshTokens
import cinescout.network.trakt.TraktAuthProvider

class RealTraktAuthProvider(
    private val dataSource: TraktAuthLocalDataSource
) : TraktAuthProvider {

    private var cachedTokens: TraktAccessAndRefreshTokens? = null

    override fun accessToken(): String? =
        getTokens()?.accessToken?.value

    override fun refreshToken(): String? =
        getTokens()?.refreshToken?.value

    private fun getTokens(): TraktAccessAndRefreshTokens? =
        cachedTokens ?: dataSource.findTokensBlocking()?.also { cachedTokens = it }
}
