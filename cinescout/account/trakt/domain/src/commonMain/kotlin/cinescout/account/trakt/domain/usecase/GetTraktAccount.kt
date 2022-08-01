package cinescout.account.trakt.domain.usecase

import arrow.core.Either
import cinescout.account.domain.model.GetAccountError
import cinescout.account.trakt.domain.model.TraktAccount
import kotlinx.coroutines.flow.Flow

class GetTraktAccount {

    operator fun invoke(): Flow<Either<GetAccountError, TraktAccount>> =
        TODO()
}
