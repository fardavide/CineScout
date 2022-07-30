package cinescout.account.tmdb.data

import arrow.core.Either
import cinescout.account.tmdb.domain.TmdbAccountRepository
import cinescout.account.tmdb.domain.model.GetAccountError
import cinescout.account.tmdb.domain.model.TmdbAccount
import cinescout.store.Store
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

class RealTmdbAccountRepository(
    private val remoteDataSource: TmdbAccountRemoteDataSource
) : TmdbAccountRepository {

    override fun getAccount(): Flow<Either<GetAccountError, TmdbAccount>> {
        return Store(
            fetch = { remoteDataSource.getAccount() },
            read = { emptyFlow() },
            write = {}
        ).map { either -> either.mapLeft { GetAccountError.Network(it.networkError) } }
    }
}
