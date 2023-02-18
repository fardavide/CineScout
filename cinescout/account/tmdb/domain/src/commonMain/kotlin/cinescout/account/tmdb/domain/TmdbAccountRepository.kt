package cinescout.account.tmdb.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.model.GetAccountError
import cinescout.account.tmdb.domain.model.TmdbAccount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import store.Refresh

interface TmdbAccountRepository {

    fun getAccount(refresh: Refresh): Flow<Either<GetAccountError, TmdbAccount>>

    suspend fun removeAccount()

    suspend fun syncAccount()
}

class FakeTmdbAccountRepository(
    private val account: TmdbAccount? = null
) : TmdbAccountRepository {

    var didSyncAccount: Boolean = false
        private set
    private val mutableAccount = MutableStateFlow(account)

    override fun getAccount(refresh: Refresh): Flow<Either<GetAccountError, TmdbAccount>> =
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
