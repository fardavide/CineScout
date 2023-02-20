package cinescout.auth.trakt.domain.usecase

import cinescout.auth.trakt.domain.TraktAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory

interface IsTraktLinked {

    operator fun invoke(): Flow<Boolean>
}

@Factory
class RealIsTraktLinked(
    private val traktAuthRepository: TraktAuthRepository
) : IsTraktLinked {

    override operator fun invoke(): Flow<Boolean> = traktAuthRepository.isLinked()
}

class FakeIsTraktLinked(private val isLinked: Boolean = false) : IsTraktLinked {

    override operator fun invoke(): Flow<Boolean> = flowOf(isLinked)
}
