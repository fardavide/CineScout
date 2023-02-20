package cinescout.account.trakt.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.model.GetAccountError
import cinescout.account.trakt.domain.model.TraktAccount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import store.Refresh

interface TraktAccountRepository {

    fun getAccount(refresh: Refresh): Flow<Either<GetAccountError, TraktAccount>>

    suspend fun removeAccount()
    suspend fun syncAccount()
}

class FakeTraktAccountRepository(account: TraktAccount? = null) : TraktAccountRepository {

    var didSyncAccount = false
        private set
    private val mutableAccount = MutableStateFlow(account)

    override fun getAccount(refresh: Refresh): Flow<Either<GetAccountError, TraktAccount>> =
        mutableAccount.map { account ->
            account?.right() ?: GetAccountError.NoAccountConnected.left()
        }

    override suspend fun removeAccount() {
        mutableAccount.emit(null)
    }

    override suspend fun syncAccount() {
        didSyncAccount = true
    }
}
