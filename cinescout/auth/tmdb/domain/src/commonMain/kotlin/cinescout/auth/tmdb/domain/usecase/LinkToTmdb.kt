package cinescout.auth.tmdb.domain.usecase

import arrow.core.Either
import cinescout.account.tmdb.domain.TmdbAccountRepository
import cinescout.auth.tmdb.domain.repository.TmdbAuthRepository
import cinescout.error.NetworkError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Factory

interface LinkToTmdb {

    operator fun invoke(): Flow<Either<Error, State>>

    sealed interface Error {
        data class Network(val networkError: NetworkError) : Error
    }

    sealed interface State {
        object Success : State
        data class UserShouldAuthorizeToken(val authorizationUrl: String) : State
    }
}

@Factory
class RealLinkToTmdb(
    private val tmdbAccountRepository: TmdbAccountRepository,
    private val tmdbAuthRepository: TmdbAuthRepository
) : LinkToTmdb {

    override operator fun invoke(): Flow<Either<LinkToTmdb.Error, LinkToTmdb.State>> =
        tmdbAuthRepository.link().onEach { either ->
            either.onRight { state ->
                if (state == LinkToTmdb.State.Success) {
                    tmdbAccountRepository.syncAccount()
                }
            }
        }
}

