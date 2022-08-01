package cinescout.account.tmdb.data

import arrow.core.Either
import cinescout.account.domain.model.GetAccountError
import cinescout.account.tmdb.domain.TmdbAccountRepository
import cinescout.account.tmdb.domain.model.TmdbAccount
import cinescout.error.NetworkError
import cinescout.store.Store
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RealTmdbAccountRepository(
    private val localDataSource: TmdbAccountLocalDataSource,
    private val remoteDataSource: TmdbAccountRemoteDataSource
) : TmdbAccountRepository {

    override fun getAccount(): Flow<Either<GetAccountError, TmdbAccount>> = Store(
        fetch = { remoteDataSource.getAccount() },
        read = { localDataSource.findAccount() },
        write = { localDataSource.insert(it) }
    ).map { either ->
        either.mapLeft { remote ->
            when (remote.networkError) {
                NetworkError.Forbidden,
                NetworkError.Internal,
                NetworkError.NoNetwork,
                NetworkError.NotFound,
                NetworkError.Unreachable -> GetAccountError.Network(remote.networkError)
                NetworkError.Unauthorized -> GetAccountError.NoAccountConnected
            }
        }
    }

    override suspend fun syncAccount() {
        remoteDataSource.getAccount()
            .tap { localDataSource.insert(it) }
    }
}
