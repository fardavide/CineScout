package cinescout.auth.tmdb.domain.usecase

import cinescout.auth.tmdb.domain.repository.TmdbAuthRepository
import org.koin.core.annotation.Factory

@Factory
class NotifyTmdbAppAuthorized(
    private val authRepository: TmdbAuthRepository
) {

    suspend operator fun invoke() {
        authRepository.notifyTokenAuthorized()
    }
}
