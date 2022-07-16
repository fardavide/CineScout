package cinescout.auth.trakt.domain.usecase

import arrow.core.Either
import cinescout.auth.trakt.domain.TraktAuthRepository
import cinescout.auth.trakt.domain.model.TraktAuthorizationCode
import cinescout.error.NetworkError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow

class LinkToTrakt(
    private val traktAuthRepository: TraktAuthRepository
) {

    operator fun invoke(): Flow<Either<Error, State>> =
        traktAuthRepository.link()

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
