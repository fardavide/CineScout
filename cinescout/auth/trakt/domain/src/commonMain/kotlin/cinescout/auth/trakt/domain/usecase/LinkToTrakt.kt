package cinescout.auth.trakt.domain.usecase

import arrow.core.Either
import cinescout.auth.trakt.domain.TraktAuthRepository
import cinescout.error.NetworkError
import kotlinx.coroutines.flow.Flow

class LinkToTrakt(
    private val traktAuthRepository: TraktAuthRepository
) {

    operator fun invoke(): Flow<Either<Error, State>> =
        traktAuthRepository.link()

    sealed interface Error {

        data class Network(val networkError: NetworkError) : Error
    }

    sealed interface State {

        object Success : State
    }
}
