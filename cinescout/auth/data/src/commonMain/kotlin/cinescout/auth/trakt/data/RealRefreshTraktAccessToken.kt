package cinescout.auth.trakt.data

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.toOption
import cinescout.auth.trakt.data.model.TraktAccessAndRefreshTokens
import cinescout.auth.trakt.data.model.TraktAccessToken
import cinescout.auth.trakt.data.model.TraktAuthState
import cinescout.auth.trakt.data.model.TraktRefreshToken
import cinescout.auth.trakt.data.service.TraktRefreshTokenService
import cinescout.network.trakt.RefreshTraktAccessToken
import cinescout.network.trakt.TraktAuthProvider
import cinescout.network.trakt.error.RefreshTokenError
import org.koin.core.annotation.Factory

@Factory
internal class RealRefreshTraktAccessToken(
    private val authProvider: TraktAuthProvider,
    private val localDataSource: TraktAuthLocalDataSource,
    private val refreshTokenService: TraktRefreshTokenService
) : RefreshTraktAccessToken {

    override suspend fun invoke(): Either<RefreshTokenError, Unit> = either {
        val refreshToken = localDataSource.findTokens()
            .toOption()
            .toEither { RefreshTokenError.NoRefreshToken }
            .bind()
            .refreshToken

        val response = refreshTokenService.createAccessToken(refreshToken)
            .mapLeft(RefreshTokenError::Network)
            .bind()

        val accessAndRefreshTokens = TraktAccessAndRefreshTokens(
            accessToken = TraktAccessToken(response.accessToken),
            refreshToken = TraktRefreshToken(response.refreshToken)
        )

        authProvider.invalidateTokens()

        localDataSource.storeAuthState(TraktAuthState.Completed(accessAndRefreshTokens))
    }
}
