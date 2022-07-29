package cinescout.auth.trakt.data

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import cinescout.auth.trakt.data.model.TraktAuthState
import cinescout.auth.trakt.domain.TraktAuthRepository
import cinescout.auth.trakt.domain.model.TraktAuthorizationCode
import cinescout.auth.trakt.domain.usecase.LinkToTrakt
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform

class RealTraktAuthRepository(
    private val dispatcher: CoroutineDispatcher,
    private val localDataSource: TraktAuthLocalDataSource,
    private val remoteDataSource: TraktAuthRemoteDataSource
) : TraktAuthRepository {

    override fun link(): Flow<Either<LinkToTrakt.Error, LinkToTrakt.State>> = localDataSource.findAuthState()
        .transform<TraktAuthState, Either<LinkToTrakt.Error, LinkToTrakt.State>> { authState ->
            either {
                when (authState) {
                    TraktAuthState.Idle -> {
                        val authorizeAppState = LinkToTrakt.State.UserShouldAuthorizeApp(
                            authorizationUrl = remoteDataSource.getAppAuthorizationUrl(),
                            authorizationResultChannel = Channel()
                        )
                        emit(authorizeAppState.right())
                    }
                    is TraktAuthState.AppAuthorized -> {
                        val authorizationCode = authState.code

                        val tokens = remoteDataSource.createAccessToken(authorizationCode)
                            .mapLeft { networkError -> LinkToTrakt.Error.Network(networkError) }
                            .tapLeft { emit(it.left()) }
                            .bind()

                        localDataSource.storeAuthState(TraktAuthState.Completed(tokens))
                    }
                    is TraktAuthState.Completed -> emit(LinkToTrakt.State.Success.right())
                }
            }
        }
        .flowOn(dispatcher)

    override suspend fun notifyAppAuthorized(code: TraktAuthorizationCode) {
        if (localDataSource.findAuthState().first() !is TraktAuthState.Idle) return
        localDataSource.storeAuthState(TraktAuthState.AppAuthorized(code))
    }
}
