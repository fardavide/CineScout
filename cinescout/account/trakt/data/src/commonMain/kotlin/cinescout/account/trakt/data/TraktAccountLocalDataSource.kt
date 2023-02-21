package cinescout.account.trakt.data

import cinescout.account.domain.model.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface TraktAccountLocalDataSource {

    suspend fun deleteAccount()
    fun findAccount(): Flow<Account.Trakt?>

    suspend fun insert(account: Account.Trakt)
}

class FakeTraktAccountLocalDataSource(
    account: Account.Trakt? = null
) : TraktAccountLocalDataSource {

    private val mutableAccount = MutableStateFlow(account)
    override suspend fun deleteAccount() {
        mutableAccount.emit(null)
    }

    override fun findAccount(): Flow<Account.Trakt?> = mutableAccount

    override suspend fun insert(account: Account.Trakt) {
        mutableAccount.emit(account)
    }
}
