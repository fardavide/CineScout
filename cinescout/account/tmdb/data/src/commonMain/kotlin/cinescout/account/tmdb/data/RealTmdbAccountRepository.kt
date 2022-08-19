package cinescout.account.tmdb.data

import arrow.core.Either
import cinescout.account.domain.model.GetAccountError
import cinescout.account.tmdb.domain.TmdbAccountRepository
import cinescout.account.tmdb.domain.model.TmdbAccount
import cinescout.error.DataError
import cinescout.error.NetworkError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import store.Store
import store.StoreKey
import store.StoreOwner

class RealTmdbAccountRepository(
    private val localDataSource: TmdbAccountLocalDataSource,
    private val remoteDataSource: TmdbAccountRemoteDataSource,
    storeOwner: StoreOwner
) : TmdbAccountRepository, StoreOwner by storeOwner {

    override fun getAccount(): Flow<Either<GetAccountError, TmdbAccount>> = Store(
        key = StoreKey(),
        fetch = { remoteDataSource.getAccount() },
        read = { localDataSource.findAccount() },
        write = { localDataSource.insert(it) }
    ).map { either ->
        either.mapLeft { dataError ->
            when (dataError) {
                DataError.Local.NoCache -> GetAccountError.NoAccountConnected
                is DataError.Remote -> when (dataError.networkError) {
                    NetworkError.Forbidden,
                    NetworkError.Internal,
                    NetworkError.NoNetwork,
                    NetworkError.NotFound,
                    NetworkError.Unreachable -> GetAccountError.Network(dataError.networkError)
                    NetworkError.Unauthorized -> GetAccountError.NoAccountConnected
                }
            }
        }
    }

    override suspend fun syncAccount() {
        remoteDataSource.getAccount()
            .tap { localDataSource.insert(it) }
    }
}
