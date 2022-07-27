package cinescout.auth.tmdb.data

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.right
import cinescout.auth.tmdb.data.model.Authorized
import cinescout.auth.tmdb.data.model.TmdbAuthState
import cinescout.auth.tmdb.domain.TmdbAuthRepository
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
import cinescout.error.NetworkError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RealTmdbAuthRepository(
    private val dispatcher: CoroutineDispatcher,
    private val localDataSource: TmdbAuthLocalDataSource,
    private val remoteDataSource: TmdbAuthRemoteDataSource
) : TmdbAuthRepository {

    override fun link() = flow<Either<LinkToTmdb.Error, LinkToTmdb.State>> {
        val result = either {

            localDataSource.storeAuthState(TmdbAuthState.Idle)

            val requestToken = remoteDataSource.createRequestToken()
                .mapToLinkError()
                .bind()

            localDataSource.storeAuthState(TmdbAuthState.RequestTokenCreated(requestToken))

            val url = "https://www.themoviedb.org/auth/access?request_token=${requestToken.value}"
            val channel = Channel<Either<LinkToTmdb.TokenNotAuthorized, LinkToTmdb.TokenAuthorized>>()
            val authorizeTokenState = LinkToTmdb.State.UserShouldAuthorizeToken(
                authorizationUrl = url,
                authorizationResultChannel = channel
            )
            emit(authorizeTokenState.right())
            channel.receive().mapLeft { LinkToTmdb.Error.UserDidNotAuthorizeToken }.bind()

            val authorizedRequestToken = Authorized(requestToken)

            localDataSource.storeAuthState(TmdbAuthState.RequestTokenAuthorized(authorizedRequestToken))

            val accessTokenAndAccountId = remoteDataSource.createAccessToken(authorizedRequestToken)
                .mapToLinkError()
                .bind()

            localDataSource.storeAuthState(TmdbAuthState.AccessTokenCreated(accessTokenAndAccountId))

            val (accessToken, accountId) = accessTokenAndAccountId
            val credentials = remoteDataSource.convertV4Session(accessToken, accountId)
                .mapToLinkError()
                .bind()

            localDataSource.storeAuthState(TmdbAuthState.Completed(credentials))

            LinkToTmdb.State.Success
        }
        emit(result)
    }.flowOn(dispatcher)

    private fun <B> Either<NetworkError, B>.mapToLinkError(): Either<LinkToTmdb.Error.Network, B> =
        mapLeft { networkError -> LinkToTmdb.Error.Network(networkError) }
}
