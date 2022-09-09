package cinescout.auth.tmdb.domain.usecase

import cinescout.auth.tmdb.domain.TmdbAuthRepository
import kotlinx.coroutines.flow.Flow

class IsTmdbLinked(
    private val tmdbAuthRepository: TmdbAuthRepository
) {

    operator fun invoke(): Flow<Boolean> =
        tmdbAuthRepository.isLinked()
}
