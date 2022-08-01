package cinescout.account.trakt.domain

import arrow.core.Either
import cinescout.account.domain.model.GetAccountError
import cinescout.account.trakt.domain.model.TraktAccount
import kotlinx.coroutines.flow.Flow

interface TraktAccountRepository {

    fun getAccount(): Flow<Either<GetAccountError, TraktAccount>>

    suspend fun syncAccount()
}
