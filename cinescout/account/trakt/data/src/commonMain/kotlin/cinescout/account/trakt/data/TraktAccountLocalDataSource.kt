package cinescout.account.trakt.data

import arrow.core.Either
import cinescout.account.trakt.domain.model.TraktAccount
import cinescout.error.DataError
import kotlinx.coroutines.flow.Flow

interface TraktAccountLocalDataSource {

    fun findAccount(): Flow<Either<DataError.Local, TraktAccount>>

    suspend fun insert(account: TraktAccount)
}
