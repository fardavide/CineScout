package cinescout.auth.tmdb.domain.usecase

import cinescout.auth.tmdb.domain.repository.TmdbAuthRepository
import org.koin.core.annotation.Factory

interface NotifyTmdbAppAuthorized {

    suspend operator fun invoke()
}

@Factory
class RealNotifyTmdbAppAuthorized(
    private val authRepository: TmdbAuthRepository
) : NotifyTmdbAppAuthorized {

    override suspend operator fun invoke() {
        authRepository.notifyTokenAuthorized()
    }
}

