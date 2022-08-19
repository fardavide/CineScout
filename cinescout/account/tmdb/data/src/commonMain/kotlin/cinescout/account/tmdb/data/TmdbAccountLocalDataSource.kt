package cinescout.account.tmdb.data

import cinescout.account.tmdb.domain.model.TmdbAccount
import kotlinx.coroutines.flow.Flow

interface TmdbAccountLocalDataSource {

    fun findAccount(): Flow<TmdbAccount?>

    suspend fun insert(account: TmdbAccount)
}
