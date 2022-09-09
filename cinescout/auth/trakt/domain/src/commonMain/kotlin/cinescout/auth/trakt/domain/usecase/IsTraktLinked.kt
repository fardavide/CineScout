package cinescout.auth.trakt.domain.usecase

import cinescout.auth.trakt.domain.TraktAuthRepository
import kotlinx.coroutines.flow.Flow

class IsTraktLinked(
    private val traktAuthRepository: TraktAuthRepository
) {

    operator fun invoke(): Flow<Boolean> =
        traktAuthRepository.isLinked()
}
