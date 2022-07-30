package cinescout.account.tmdb.data

import arrow.core.Either
import cinescout.account.tmdb.domain.model.TmdbAccount
import cinescout.error.DataError
import kotlinx.coroutines.flow.Flow

interface TmdbAccountLocalDataSource {

    fun findAccount(): Flow<Either<DataError.Local, TmdbAccount>>

    suspend fun insert(account: TmdbAccount)
}
