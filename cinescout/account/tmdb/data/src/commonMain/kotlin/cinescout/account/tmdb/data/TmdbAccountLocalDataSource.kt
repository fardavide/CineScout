package cinescout.account.tmdb.data

import cinescout.account.domain.model.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface TmdbAccountLocalDataSource {

    suspend fun deleteAccount()

    fun findAccount(): Flow<Account.Tmdb?>

    suspend fun insert(account: Account.Tmdb)
}

class FakeTmdbAccountLocalDataSource(
    account: Account.Tmdb? = null
) : TmdbAccountLocalDataSource {

    private val mutableAccount = MutableStateFlow(account)

    override suspend fun deleteAccount() {
        mutableAccount.emit(null)
    }

    override fun findAccount(): Flow<Account.Tmdb?> = mutableAccount

    override suspend fun insert(account: Account.Tmdb) {
        mutableAccount.emit(account)
    }
}
