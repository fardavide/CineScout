package cinescout.auth.tmdb.domain.usecase

import cinescout.auth.tmdb.domain.repository.TmdbAuthRepository
import org.koin.core.annotation.Factory

@Factory
class UnlinkFromTmdb(
    private val tmdbAuthRepository: TmdbAuthRepository
) {

    suspend operator fun invoke() {
        tmdbAuthRepository.unlink()
    }
}
