package cinescout.store5.ext

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.store5.Store5ReadResponse
import cinescout.store5.StoreFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map

fun <Output : Any> StoreFlow<Output>.filterData(): Flow<Either<NetworkError, Output>> =
    filterIsInstance<Store5ReadResponse.Data<Output>>().map { it.value }
