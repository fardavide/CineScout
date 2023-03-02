package cinescout.auth.trakt.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.auth.trakt.data.model.TraktAccessAndRefreshTokens
import cinescout.auth.trakt.data.sample.TraktAccessAndRefreshTokensSample
import cinescout.auth.trakt.domain.model.TraktAuthorizationCode
import cinescout.error.NetworkError

interface TraktAuthRemoteDataSource {

    suspend fun createAccessToken(
        authorizationCode: TraktAuthorizationCode
    ): Either<NetworkError, TraktAccessAndRefreshTokens>

    fun getAppAuthorizationUrl(): String
}

class FakeTraktAuthRemoteDataSource(
    private val accessAndRefreshTokens: TraktAccessAndRefreshTokens =
        TraktAccessAndRefreshTokensSample.Tokens,
    private var createAccessTokenFirstCallError: NetworkError? = null
) : TraktAuthRemoteDataSource {

    override suspend fun createAccessToken(
        authorizationCode: TraktAuthorizationCode
    ): Either<NetworkError, TraktAccessAndRefreshTokens> = createAccessTokenFirstCallError?.left()
        ?.also { createAccessTokenFirstCallError = null }
        ?: accessAndRefreshTokens.right()

    override fun getAppAuthorizationUrl(): String = "https://trakt.tv/oauth/authorize"
}
