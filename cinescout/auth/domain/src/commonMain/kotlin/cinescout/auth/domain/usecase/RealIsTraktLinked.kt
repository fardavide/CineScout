package cinescout.auth.domain.usecase

import cinescout.auth.domain.TraktAuthRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class RealIsTraktLinked(
    private val traktAuthRepository: TraktAuthRepository
) : IsTraktLinked {

    override operator fun invoke(): Flow<Boolean> = traktAuthRepository.isLinked()
}

