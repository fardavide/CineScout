package cinescout.account.domain.store

import arrow.core.Either
import cinescout.CineScoutTestApi
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.GetAccountError
import cinescout.error.NetworkError
import cinescout.notImplementedFake
import cinescout.store5.Store5
import cinescout.store5.StoreFlow
import cinescout.store5.ext.filterData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface AccountStore : Store5<Unit, Account>

@CineScoutTestApi
class FakeAccountStore : AccountStore {

    override suspend fun clear() {
        notImplementedFake()
    }

    override fun stream(request: StoreReadRequest<Unit>): StoreFlow<Account> {
        notImplementedFake()
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
