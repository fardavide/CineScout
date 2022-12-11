package cinescout.auth.trakt.domain.usecase

import cinescout.auth.trakt.domain.TraktAuthRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class IsTraktLinked(
    private val traktAuthRepository: TraktAuthRepository
) {

    operator fun invoke(): Flow<Boolean> =
        traktAuthRepository.isLinked()
}
