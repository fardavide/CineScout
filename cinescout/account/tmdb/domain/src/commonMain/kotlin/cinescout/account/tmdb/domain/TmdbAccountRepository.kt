package cinescout.account.tmdb.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.GetAccountError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import store.Refresh

interface TmdbAccountRepository {

    fun getAccount(refresh: Refresh): Flow<Either<GetAccountError, Account.Tmdb>>

    suspend fun removeAccount()

    suspend fun syncAccount()
}

class FakeTmdbAccountRepository(account: Account.Tmdb? = null) : TmdbAccountRepository {

    var didSyncAccount: Boolean = false
        private set
    private val mutableAccount = MutableStateFlow(account)

    override fun getAccount(refresh: Refresh): Flow<Either<GetAccountError, Account.Tmdb>> =
        mutableAccount.map { account ->
            account?.right() ?: GetAccountError.NoAccountConnected.left()
        }

    override suspend fun syncAccount() {
        didSyncAccount = true
    }

    override suspend fun removeAccount() {
        mutableAccount.emit(null)
    }
}
