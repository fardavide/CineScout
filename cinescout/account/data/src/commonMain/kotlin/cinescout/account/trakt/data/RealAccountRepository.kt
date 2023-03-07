package cinescout.account.trakt.data

import arrow.core.Either
import cinescout.account.domain.AccountRepository
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.GetAccountError
import cinescout.error.DataError
import cinescout.error.NetworkError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import store.Fetcher
import store.Reader
import store.Refresh
import store.Store
import store.StoreKey
import store.StoreOwner

@Factory
class RealAccountRepository(
    private val localDataSource: TraktAccountLocalDataSource,
    private val remoteDataSource: TraktAccountRemoteDataSource,
    private val storeOwner: StoreOwner
) : AccountRepository, StoreOwner by storeOwner {

    override fun getAccount(refresh: Refresh): Flow<Either<GetAccountError, Account>> = Store(
        key = StoreKey("trakt_account"),
        refresh = refresh,
        fetcher = Fetcher.forOperation { remoteDataSource.getAccount() },
        reader = Reader.fromSource { localDataSource.findAccount() },
        write = { localDataSource.insert(it) }
    ).map { either ->
        either.mapLeft { dataError ->
            when (dataError) {
                DataError.Local.NoCache -> GetAccountError.NotConnected
                is DataError.Remote -> when (dataError.networkError) {
                    NetworkError.BadRequest,
                    NetworkError.Forbidden,
                    NetworkError.Internal,
                    NetworkError.NoNetwork,
                    NetworkError.NotFound,
                    NetworkError.Unknown,
                    NetworkError.Unreachable -> GetAccountError.Network(dataError.networkError)

                    NetworkError.Unauthorized -> GetAccountError.NotConnected
                }
            }
        }
    }

    override suspend fun removeAccount() {
        localDataSource.deleteAccount()
    }

    override suspend fun syncAccount() {
        remoteDataSource.getAccount()
            .onRight { localDataSource.insert(it) }
    }
}