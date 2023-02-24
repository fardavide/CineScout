package cinescout.network.trakt

import arrow.core.Either
import cinescout.network.trakt.error.RefreshTokenError

interface RefreshTraktAccessToken {

    suspend operator fun invoke(): Either<RefreshTokenError, Unit>
}
