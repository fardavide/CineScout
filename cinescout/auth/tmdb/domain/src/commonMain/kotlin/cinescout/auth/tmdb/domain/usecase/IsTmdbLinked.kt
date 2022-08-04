package cinescout.auth.tmdb.domain.usecase

import cinescout.auth.tmdb.domain.TmdbAuthRepository

class IsTmdbLinked(
    private val tmdbAuthRepository: TmdbAuthRepository
) {

    suspend operator fun invoke() = tmdbAuthRepository.isLinked()
}
