package cinescout.auth.tmdb.data

import arrow.core.Either
import arrow.core.right
import cinescout.auth.tmdb.data.model.TmdbAccessToken
import cinescout.auth.tmdb.data.model.TmdbAccessTokenAndAccountId
import cinescout.auth.tmdb.data.model.TmdbAccountId
import cinescout.auth.tmdb.data.model.TmdbAuthorizedRequestToken
import cinescout.auth.tmdb.data.model.TmdbCredentials
import cinescout.auth.tmdb.data.model.TmdbRequestToken
import cinescout.auth.tmdb.data.sample.TmdbAccessTokenAndAccountIdSample
import cinescout.auth.tmdb.data.sample.TmdbCredentialsSample
import cinescout.auth.tmdb.data.sample.TmdbRequestTokenSample
import cinescout.error.NetworkError

interface TmdbAuthRemoteDataSource {

    suspend fun createRequestToken(): Either<NetworkError, TmdbRequestToken>

    suspend fun createAccessToken(
        token: TmdbAuthorizedRequestToken
    ): Either<NetworkError, TmdbAccessTokenAndAccountId>

    suspend fun convertV4Session(
        accessToken: TmdbAccessToken,
        accountId: TmdbAccountId
    ): Either<NetworkError, TmdbCredentials>

    fun getTokenAuthorizationUrl(requestToken: TmdbRequestToken): String
}

class FakeTmdbAuthRemoteDataSource(
    private val requestToken: TmdbRequestToken = TmdbRequestTokenSample.RequestToken,
    private val accessTokenAndAccountId: TmdbAccessTokenAndAccountId =
        TmdbAccessTokenAndAccountIdSample.AccessTokenAndAccountId,
    private val credentials: TmdbCredentials = TmdbCredentialsSample.Credentials
) : TmdbAuthRemoteDataSource {

    override suspend fun createRequestToken(): Either<NetworkError, TmdbRequestToken> = requestToken.right()

    override suspend fun createAccessToken(
        token: TmdbAuthorizedRequestToken
    ): Either<NetworkError, TmdbAccessTokenAndAccountId> = accessTokenAndAccountId.right()

    override suspend fun convertV4Session(
        accessToken: TmdbAccessToken,
        accountId: TmdbAccountId
    ): Either<NetworkError, TmdbCredentials> = credentials.right()

    override fun getTokenAuthorizationUrl(requestToken: TmdbRequestToken): String =
        "https://www.themoviedb.org/authenticate/${requestToken.value}"
}
