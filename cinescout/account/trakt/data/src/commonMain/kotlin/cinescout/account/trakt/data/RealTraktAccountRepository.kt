package cinescout.account.trakt.data

import arrow.core.Either
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.GetAccountError
import cinescout.account.trakt.domain.TraktAccountRepository
import cinescout.error.DataError
import cinescout.error.NetworkError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import store.Fetcher
import store.Refresh
import store.Store
import store.StoreKey
import store.StoreOwner

@Factory
class RealTraktAccountRepository(
    private val localDataSource: TraktAccountLocalDataSource,
    private val remoteDataSource: TraktAccountRemoteDataSource,
    private val storeOwner: StoreOwner
) : TraktAccountRepository, StoreOwner by storeOwner {

    override fun getAccount(refresh: Refresh): Flow<Either<GetAccountError, Account.Trakt>> = Store(
        key = StoreKey("trakt_account"),
        refresh = refresh,
        fetch = Fetcher.forOperation { remoteDataSource.getAccount() },
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
                    NetworkError.Unknown,
                    NetworkError.Unreachable -> GetAccountError.Network(dataError.networkError)

                    NetworkError.Unauthorized -> GetAccountError.NoAccountConnected
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
