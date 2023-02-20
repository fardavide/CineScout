package cinescout.account.tmdb.data

import cinescout.account.tmdb.domain.model.TmdbAccount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface TmdbAccountLocalDataSource {

    suspend fun deleteAccount()

    fun findAccount(): Flow<TmdbAccount?>

    suspend fun insert(account: TmdbAccount)
}

class FakeTmdbAccountLocalDataSource(
    account: TmdbAccount? = null
) : TmdbAccountLocalDataSource {

    private val mutableAccount = MutableStateFlow(account)

    override suspend fun deleteAccount() {
        mutableAccount.emit(null)
    }

    override fun findAccount(): Flow<TmdbAccount?> = mutableAccount

    override suspend fun insert(account: TmdbAccount) {
        mutableAccount.emit(account)
    }
}
