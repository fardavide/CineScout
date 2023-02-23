package cinescout.auth.tmdb.domain.usecase

import cinescout.auth.domain.usecase.IsTmdbLinked
import cinescout.auth.tmdb.domain.repository.TmdbAuthRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class RealIsTmdbLinked(
    private val tmdbAuthRepository: TmdbAuthRepository
) : IsTmdbLinked {

    override fun invoke(): Flow<Boolean> = tmdbAuthRepository.isLinked()
}

