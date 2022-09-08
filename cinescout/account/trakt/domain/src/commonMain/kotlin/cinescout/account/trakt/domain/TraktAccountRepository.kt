package cinescout.account.trakt.domain

import arrow.core.Either
import cinescout.account.domain.model.GetAccountError
import cinescout.account.trakt.domain.model.TraktAccount
import kotlinx.coroutines.flow.Flow
import store.Refresh

interface TraktAccountRepository {

    fun getAccount(refresh: Refresh): Flow<Either<GetAccountError, TraktAccount>>

    suspend fun syncAccount()
}
