package cinescout.account.tmdb.domain

import arrow.core.Either
import cinescout.account.domain.model.GetAccountError
import cinescout.account.tmdb.domain.model.TmdbAccount
import kotlinx.coroutines.flow.Flow
import store.Refresh

interface TmdbAccountRepository {

    fun getAccount(refresh: Refresh): Flow<Either<GetAccountError, TmdbAccount>>

    suspend fun syncAccount()
}
