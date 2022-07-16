package cinescout.auth.trakt.data

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.right
import cinescout.auth.trakt.domain.TraktAuthRepository
import cinescout.auth.trakt.domain.usecase.LinkToTrakt
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RealTraktAuthRepository(
    private val dispatcher: CoroutineDispatcher,
    private val localDataSource: TraktAuthLocalDataSource,
    private val remoteDataSource: TraktAuthRemoteDataSource
) : TraktAuthRepository {

    override fun link() = flow<Either<LinkToTrakt.Error, LinkToTrakt.State>> {
        val result = either<LinkToTrakt.Error, LinkToTrakt.State> {
            val channel = Channel<Either<LinkToTrakt.AppNotAuthorized, LinkToTrakt.AppAuthorized>>()
            val authorizeAppState = LinkToTrakt.State.UserShouldAuthorizeApp(
                authorizationUrl = remoteDataSource.getAppAuthorizationUrl(),
                authorizationResultChannel = channel
            )
            emit(authorizeAppState.right())

            val authorizationCode = channel.receive()
                .mapLeft { LinkToTrakt.Error.UserDidNotAuthorizeApp }
                .bind()
                .code

            val tokens = remoteDataSource.createAccessToken(authorizationCode)
                .mapLeft { networkError -> LinkToTrakt.Error.Network(networkError) }
                .bind()

            localDataSource.storeTokens(tokens)
            LinkToTrakt.State.Success
        }
        emit(result)
    }.flowOn(dispatcher)
}
