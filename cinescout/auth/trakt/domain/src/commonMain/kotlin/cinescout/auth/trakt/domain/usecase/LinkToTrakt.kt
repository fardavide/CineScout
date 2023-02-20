package cinescout.auth.trakt.domain.usecase

import arrow.core.Either
import arrow.core.right
import cinescout.account.trakt.domain.TraktAccountRepository
import cinescout.auth.trakt.domain.TraktAuthRepository
import cinescout.auth.trakt.domain.model.TraktAuthorizationCode
import cinescout.error.NetworkError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Factory

interface LinkToTrakt {

    operator fun invoke(): Flow<Either<Error, State>>

    sealed interface Error {

        data class Network(val networkError: NetworkError) : Error
        object UserDidNotAuthorizeApp : Error
    }

    sealed interface State {

        object Success : State

        data class UserShouldAuthorizeApp(
            val authorizationUrl: String,
            val authorizationResultChannel: Channel<Either<AppNotAuthorized, AppAuthorized>>
        ) : State
    }

    data class AppAuthorized(val code: TraktAuthorizationCode)
    object AppNotAuthorized
}

@Factory
class RealLinkToTrakt(
    private val traktAccountRepository: TraktAccountRepository,
    private val traktAuthRepository: TraktAuthRepository
) : LinkToTrakt {

    override operator fun invoke(): Flow<Either<LinkToTrakt.Error, LinkToTrakt.State>> =
        traktAuthRepository.link().onEach { either ->
            either.onRight { state ->
                if (state == LinkToTrakt.State.Success) {
                    traktAccountRepository.syncAccount()
                }
            }
        }
}

class FakeLinkToTrakt(
    state: LinkToTrakt.State = LinkToTrakt.State.Success,
    private val result: Either<LinkToTrakt.Error, LinkToTrakt.State> = state.right()
) : LinkToTrakt {

    var invoked = false
        private set

    override operator fun invoke(): Flow<Either<LinkToTrakt.Error, LinkToTrakt.State>> {
        invoked = true
        return flowOf(result)
    }
}
