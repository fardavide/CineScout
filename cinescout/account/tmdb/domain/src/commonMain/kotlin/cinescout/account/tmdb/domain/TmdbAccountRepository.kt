package cinescout.account.tmdb.domain

import arrow.core.Either
import cinescout.account.domain.model.GetAccountError
import cinescout.account.tmdb.domain.model.TmdbAccount
import kotlinx.coroutines.flow.Flow

interface TmdbAccountRepository {

    fun getAccount(): Flow<Either<GetAccountError, TmdbAccount>>

    suspend fun syncAccount()
}
