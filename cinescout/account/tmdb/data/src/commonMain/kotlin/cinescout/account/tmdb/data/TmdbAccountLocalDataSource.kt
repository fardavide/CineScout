package cinescout.account.tmdb.data

import cinescout.account.tmdb.domain.model.TmdbAccount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface TmdbAccountLocalDataSource {

    fun findAccount(): Flow<TmdbAccount?>

    suspend fun insert(account: TmdbAccount)

    suspend fun deleteAccount()
}

class FakeTmdbAccountLocalDataSource(
    account: TmdbAccount? = null
) : TmdbAccountLocalDataSource {

    private val mutableAccount = MutableStateFlow(account)

    override fun findAccount(): Flow<TmdbAccount?> = mutableAccount

    override suspend fun insert(account: TmdbAccount) {
        mutableAccount.emit(account)
    }

    override suspend fun deleteAccount() {
        mutableAccount.emit(null)
    }
}
