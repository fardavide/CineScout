package cinescout.auth.tmdb.domain.usecase

import arrow.core.Either
import cinescout.auth.tmdb.domain.TmdbAuthRepository
import cinescout.error.NetworkError
import kotlinx.coroutines.flow.Flow

class LinkToTmdb(
    private val tmdbAuthRepository: TmdbAuthRepository
) {

    operator fun invoke(): Flow<Either<Error, State>> =
        tmdbAuthRepository.link()

    sealed interface Error {
        data class Network(val networkError: NetworkError) : Error
        object UserDidNotAuthorizeToken : Error
    }

    sealed interface State {
        object Success : State
        data class UserShouldAuthorizeToken(val authorizationUrl: String) : State
    }

    object TokenAuthorized
    object TokenNotAuthorized
}
