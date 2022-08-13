package cinescout.auth.tmdb.domain.usecase

import arrow.core.Either
import cinescout.account.tmdb.domain.usecase.SyncTmdbAccount
import cinescout.auth.tmdb.domain.TmdbAuthRepository
import cinescout.error.NetworkError
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.usecase.StartUpdateSuggestedMovies
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class LinkToTmdb(
    private val startUpdateSuggestedMovies: StartUpdateSuggestedMovies,
    private val syncTmdbAccount: SyncTmdbAccount,
    private val tmdbAuthRepository: TmdbAuthRepository
) {

    operator fun invoke(): Flow<Either<Error, State>> =
        tmdbAuthRepository.link()
            .onEach { either ->
                either.tap { state ->
                    if (state == State.Success) {
                        syncTmdbAccount()
                        startUpdateSuggestedMovies(SuggestionsMode.Quick)
                    }
                }
            }

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
