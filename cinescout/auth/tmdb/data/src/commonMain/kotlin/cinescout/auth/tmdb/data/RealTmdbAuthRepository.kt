package cinescout.auth.tmdb.data

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import cinescout.auth.tmdb.data.model.Authorized
import cinescout.auth.tmdb.data.model.TmdbAuthState
import cinescout.auth.tmdb.domain.TmdbAuthRepository
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
import cinescout.error.NetworkError
import cinescout.utils.kotlin.DispatcherQualifier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
class RealTmdbAuthRepository(
    @Named(DispatcherQualifier.Io) private val dispatcher: CoroutineDispatcher,
    private val localDataSource: TmdbAuthLocalDataSource,
    private val remoteDataSource: TmdbAuthRemoteDataSource
) : TmdbAuthRepository {

    override fun isLinked(): Flow<Boolean> =
        localDataSource.findAuthState().map { it is TmdbAuthState.Completed }

    override fun link(): Flow<Either<LinkToTmdb.Error, LinkToTmdb.State>> = localDataSource.findAuthState()
        .transform<TmdbAuthState, Either<LinkToTmdb.Error, LinkToTmdb.State>> { authState ->
            either {
                when (authState) {
                    TmdbAuthState.Idle -> {
                        val requestToken = remoteDataSource.createRequestToken()
                            .mapToLinkError()
                            .onLeft { emit(it.left()) }
                            .bind()

                        localDataSource.storeAuthState(TmdbAuthState.RequestTokenCreated(requestToken))
                    }
                    is TmdbAuthState.RequestTokenCreated -> {
                        val requestToken = authState.requestToken

                        val authorizeTokenState = LinkToTmdb.State.UserShouldAuthorizeToken(
                            authorizationUrl = remoteDataSource.getTokenAuthorizationUrl(requestToken)
                        )
                        emit(authorizeTokenState.right())
                    }
                    is TmdbAuthState.RequestTokenAuthorized -> {
                        val authorizedRequestToken = authState.requestToken

                        val accessTokenAndAccountId = remoteDataSource.createAccessToken(authorizedRequestToken)
                            .mapToLinkError()
                            .onLeft { emit(it.left()) }
                            .bind()

                        localDataSource.storeAuthState(TmdbAuthState.AccessTokenCreated(accessTokenAndAccountId))
                    }
                    is TmdbAuthState.AccessTokenCreated -> {
                        val (accessToken, accountId) = authState.accessTokenAndAccountId
                        val credentials = remoteDataSource.convertV4Session(accessToken, accountId)
                            .mapToLinkError()
                            .onLeft { emit(it.left()) }
                            .bind()

                        localDataSource.storeAuthState(TmdbAuthState.Completed(credentials))
                    }
                    is TmdbAuthState.Completed -> emit(LinkToTmdb.State.Success.right())
                }
            }
        }
        .flowOn(dispatcher)

    override suspend fun notifyTokenAuthorized() {
        val currentState = localDataSource.findAuthState().first() as? TmdbAuthState.RequestTokenCreated
            ?: return
        localDataSource.storeAuthState(TmdbAuthState.RequestTokenAuthorized(Authorized(currentState.requestToken)))
    }

    private fun <B> Either<NetworkError, B>.mapToLinkError(): Either<LinkToTmdb.Error.Network, B> =
        mapLeft { networkError -> LinkToTmdb.Error.Network(networkError) }
}
