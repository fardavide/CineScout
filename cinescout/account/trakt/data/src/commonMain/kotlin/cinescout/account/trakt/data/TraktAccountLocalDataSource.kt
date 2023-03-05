package cinescout.account.trakt.data

import cinescout.account.domain.model.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface TraktAccountLocalDataSource {

    suspend fun deleteAccount()
    fun findAccount(): Flow<Account?>

    suspend fun insert(account: Account)
}

class FakeTraktAccountLocalDataSource(
    account: Account? = null
) : TraktAccountLocalDataSource {

    private val mutableAccount = MutableStateFlow(account)
    override suspend fun deleteAccount() {
        mutableAccount.emit(null)
    }

    override fun findAccount(): Flow<Account?> = mutableAccount

    override suspend fun insert(account: Account) {
        mutableAccount.emit(account)
    }
}
