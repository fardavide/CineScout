package cinescout.auth.tmdb.domain.usecase

import cinescout.auth.tmdb.domain.repository.TmdbAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory

interface IsTmdbLinked {

    operator fun invoke(): Flow<Boolean>
}

@Factory
internal class RealIsTmdbLinked(
    private val tmdbAuthRepository: TmdbAuthRepository
) : IsTmdbLinked {

    override fun invoke(): Flow<Boolean> = tmdbAuthRepository.isLinked()
}

class FakeIsTmdbLinked(private val isLinked: Boolean) : IsTmdbLinked {

    override fun invoke(): Flow<Boolean> = flowOf(isLinked)
}
