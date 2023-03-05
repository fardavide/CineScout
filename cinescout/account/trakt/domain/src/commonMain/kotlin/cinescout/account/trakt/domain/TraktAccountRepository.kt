package cinescout.account.trakt.domain

import arrow.core.Either
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.GetAccountError
import kotlinx.coroutines.flow.Flow
import store.Refresh

interface TraktAccountRepository {

    fun getAccount(refresh: Refresh): Flow<Either<GetAccountError, Account>>

    suspend fun removeAccount()
    suspend fun syncAccount()
}

