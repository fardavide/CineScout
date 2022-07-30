package cinescout.account.tmdb.data

import arrow.core.Either
import arrow.core.left
import cinescout.account.tmdb.domain.TmdbAccountRepository
import cinescout.account.tmdb.domain.model.GetAccountError
import cinescout.account.tmdb.domain.model.TmdbAccount
import cinescout.error.DataError
import cinescout.error.NetworkError
import cinescout.store.Store
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class RealTmdbAccountRepository(
    private val remoteDataSource: TmdbAccountRemoteDataSource
) : TmdbAccountRepository {

    override fun getAccount(): Flow<Either<GetAccountError, TmdbAccount>> {
        return Store(
            fetch = { remoteDataSource.getAccount() },
            read = { flowOf(DataError.Local.NoCache.left()) },
            write = {}
        ).map { either ->
            either.mapLeft { remote ->
                when (val networkError = remote.networkError) {
                    NetworkError.Forbidden,
                    NetworkError.Internal,
                    NetworkError.NoNetwork,
                    NetworkError.NotFound,
                    NetworkError.Unreachable -> GetAccountError.Network(remote.networkError)
                    NetworkError.Unauthorized -> GetAccountError.NoAccountConnected
                }
            }
        }
    }
}
