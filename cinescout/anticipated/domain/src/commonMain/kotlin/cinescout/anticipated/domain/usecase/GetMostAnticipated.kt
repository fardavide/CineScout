package cinescout.anticipated.domain.usecase

import arrow.core.Either
import cinescout.anticipated.domain.store.MostAnticipatedStore
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.store5.ext.filterData
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

@Factory
class GetMostAnticipated(
    private val mostAnticipatedStore: MostAnticipatedStore
) {

    operator fun invoke(
        type: ScreenplayType,
        refresh: Boolean
    ): Flow<Either<NetworkError, List<Screenplay>>> =
        mostAnticipatedStore.stream(StoreReadRequest.cached(MostAnticipatedStore.Key(type), refresh)).filterData()
}
