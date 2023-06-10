package cinescout.auth.trakt.data

import arrow.core.Either
import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import cinescout.auth.domain.TraktAuthRepository
import cinescout.auth.domain.model.TraktAuthorizationCode
import cinescout.auth.domain.usecase.LinkToTrakt
import cinescout.auth.trakt.data.model.TraktAuthState
import cinescout.error.NetworkError
import cinescout.utils.kotlin.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
class RealTraktAuthRepository(
    @Named(IoDispatcher) private val dispatcher: CoroutineDispatcher,
    private val localDataSource: TraktAuthLocalDataSource,
    private val remoteDataSource: TraktAuthRemoteDataSource
) : TraktAuthRepository {

    override fun isLinked(): Flow<Boolean> =
        localDataSource.findAuthState().map { it is TraktAuthState.Completed }

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

                        remoteDataSource.createAccessToken(authorizationCode)
                            .onLeft { networkError ->
                                if (networkError is NetworkError.BadRequest) {
                                    localDataSource.storeAuthState(TraktAuthState.Idle)
                                } else {
                                    emit(LinkToTrakt.Error.Network(networkError).left())
                                }
                            }
                            .onRight { tokens ->
                                localDataSource.storeAuthState(TraktAuthState.Completed(tokens))
                            }
                            .bind()
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

    override suspend fun unlink() {
        with(localDataSource) {
            deleteTokens()
            storeAuthState(TraktAuthState.Idle)
        }
    }
}
