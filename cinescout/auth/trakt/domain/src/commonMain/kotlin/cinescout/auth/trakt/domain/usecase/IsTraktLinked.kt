package cinescout.auth.trakt.domain.usecase

import cinescout.auth.trakt.domain.TraktAuthRepository

class IsTraktLinked(
    private val traktAuthRepository: TraktAuthRepository
) {

    suspend operator fun invoke() = traktAuthRepository.isLinked()
}
