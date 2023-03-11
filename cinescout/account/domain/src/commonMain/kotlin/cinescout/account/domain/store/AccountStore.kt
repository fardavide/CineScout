package cinescout.account.domain.store

import arrow.core.Either
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.GetAccountError
import cinescout.error.NetworkError
import cinescout.store5.Store5
import cinescout.store5.StoreFlow
import cinescout.store5.ext.filterData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface AccountStore : Store5<Unit, Account>

class FakeAccountStore : AccountStore {

    override suspend fun clear() {
        TODO("Not yet implemented")
    }

    override fun stream(request: StoreReadRequest<Unit>): StoreFlow<Account> {
        TODO("Not yet implemented")
    }
}

fun StoreFlow<Account>.withAccountError(): Flow<Either<GetAccountError, Account>> = filterData()
    .map { either -> either.mapLeft(::toAccountError) }

private fun toAccountError(networkError: NetworkError) = when (networkError) {
    NetworkError.BadRequest,
    NetworkError.Forbidden,
    NetworkError.Internal,
    NetworkError.NoNetwork,
    NetworkError.NotFound,
    NetworkError.Unknown,
    NetworkError.Unreachable -> GetAccountError.Network(networkError)
    NetworkError.Unauthorized -> GetAccountError.NotConnected
}
