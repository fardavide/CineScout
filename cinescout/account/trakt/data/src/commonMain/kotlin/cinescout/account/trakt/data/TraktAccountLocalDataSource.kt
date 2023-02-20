package cinescout.account.trakt.data

import cinescout.account.trakt.domain.model.TraktAccount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface TraktAccountLocalDataSource {

    suspend fun deleteAccount()
    fun findAccount(): Flow<TraktAccount?>

    suspend fun insert(account: TraktAccount)
}

class FakeTraktAccountLocalDataSource(
    account: TraktAccount? = null
) : TraktAccountLocalDataSource {

    private val mutableAccount = MutableStateFlow(account)
    override suspend fun deleteAccount() {
        mutableAccount.emit(null)
    }

    override fun findAccount(): Flow<TraktAccount?> = mutableAccount

    override suspend fun insert(account: TraktAccount) {
        mutableAccount.emit(account)
    }
}
