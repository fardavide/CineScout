package cinescout.account.domain.usecase

import arrow.core.Either
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.GetAccountError
import kotlinx.coroutines.flow.Flow
import store.Refresh

interface GetTmdbAccount {

    operator fun invoke(
        refresh: Refresh = Refresh.WithInterval()
    ): Flow<Either<GetAccountError, Account.Tmdb>>
}
