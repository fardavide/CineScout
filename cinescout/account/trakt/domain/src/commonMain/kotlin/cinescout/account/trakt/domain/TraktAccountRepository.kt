package cinescout.account.trakt.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.GetAccountError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import store.Refresh

interface TraktAccountRepository {

    fun getAccount(refresh: Refresh): Flow<Either<GetAccountError, Account.Trakt>>

    suspend fun removeAccount()
    suspend fun syncAccount()
}

class FakeTraktAccountRepository(account: Account.Trakt? = null) : TraktAccountRepository {

    var didSyncAccount = false
        private set
    private val mutableAccount = MutableStateFlow(account)

    override fun getAccount(refresh: Refresh): Flow<Either<GetAccountError, Account.Trakt>> =
        mutableAccount.map { account ->
            account?.right() ?: GetAccountError.NotConnected.left()
        }

    override suspend fun removeAccount() {
        mutableAccount.emit(null)
    }

    override suspend fun syncAccount() {
        didSyncAccount = true
    }
}
