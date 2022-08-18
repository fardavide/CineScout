package cinescout.account.trakt.data

import arrow.core.Either
import cinescout.account.domain.model.GetAccountError
import cinescout.account.trakt.domain.TraktAccountRepository
import cinescout.account.trakt.domain.model.TraktAccount
import cinescout.error.DataError
import cinescout.error.NetworkError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import store.Store

class RealTraktAccountRepository(
    private val localDataSource: TraktAccountLocalDataSource,
    private val remoteDataSource: TraktAccountRemoteDataSource
) : TraktAccountRepository {

    override fun getAccount(): Flow<Either<GetAccountError, TraktAccount>> = Store(
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
